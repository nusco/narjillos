package org.nusco.narjillos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.utilities.Light;
import org.nusco.narjillos.utilities.Locator;
import org.nusco.narjillos.utilities.PetriDishState;
import org.nusco.narjillos.utilities.Speed;
import org.nusco.narjillos.utilities.Viewport;
import org.nusco.narjillos.views.DataView;
import org.nusco.narjillos.views.EcosystemView;

public class PetriDish extends Application {

	private static final int FRAMES_PER_SECOND_WITH_LIGHT_ON = 30;
	private static final int FRAMES_PER_SECOND_WITH_LIGHT_OFF = 5;
	private static final int FRAMES_PERIOD_WITH_LIGHT_ON = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_ON;
	private static final int FRAMES_PERIOD_WITH_LIGHT_OFF = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_OFF;
	private static final long PAN_SPEED = 200;

	private static String[] programArguments = new String[0];

	private Lab lab;
	private volatile EcosystemView ecosystemView;
	private Locator locator;
	private DataView dataView;

	private Node foreground;
	private final PetriDishState state = new PetriDishState();
	private final Chronometer framesChronometer = new Chronometer();
	private volatile Thing lockedOn = Thing.NULL;

	@Override
	public void start(final Stage primaryStage) {
		final Group root = new Group();

		startModelThread(programArguments);

		ecosystemView = new EcosystemView(lab.getEcosystem());
		locator = new Locator(lab.getEcosystem());
		dataView = new DataView(lab);

		updateForeground();
		update(root);
		startViewThread(root);

		Viewport viewport = getEcosystemView().getViewport();
		Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);
		scene.setOnKeyPressed(createKeyboardHandler());
		scene.setOnMouseClicked(createMouseHandler());
		scene.setOnScroll(createMouseScrollHandler());
		bindViewportSizeToWindowSize(scene, viewport);

		primaryStage.setTitle("Narjillos - Petri Dish");
		primaryStage.setScene(scene);

		// run GC to avoid it kicking off during
		// the first stages of animation
		System.gc();

		primaryStage.show();
	}

	private Viewport getViewport() {
		return getEcosystemView().getViewport();
	}

	private EcosystemView getEcosystemView() {
		return ecosystemView;
	}

	private void startModelThread(final String[] arguments) {
		final boolean[] isInitializationDone = new boolean[] { false };
		
		Thread modelThread = new Thread() {
			@Override
			public void run() {
				// We need to do initialize the lab here,
				// because the random number generator will
				// complain if it is called from different
				// threads.
				lab = new Lab(arguments);
				
				isInitializationDone[0] = true;

				while (true) {
					long startTime = System.currentTimeMillis();
					if (state.getSpeed() != Speed.PAUSED)
						if (!lab.tick())
							System.exit(0);
					waitFor(state.getSpeed().getTicksPeriod(), startTime);
				}
			}
		};
		
		modelThread.setDaemon(true);
		modelThread.start();
		while (!isInitializationDone[0])
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
	}

	private void startViewThread(final Group root) {
		Task<Void> task = new Task<Void>() {
			private volatile boolean renderingFinished = false;

			@Override
			public Void call() throws Exception {
				while (true) {
					long startTime = System.currentTimeMillis();
					renderingFinished = false;

					if (isLocked()) {
						Narjillo narjillo = (Narjillo) lockedOn;
						if (narjillo.isDead())
							unlock();
						else
							getViewport().flyToTargetEC(narjillo.calculateCenterOfMass());
					}

					ecosystemView.tick();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							update(root);
							renderingFinished = true;
						}
					});
					waitFor(getFramesPeriod(), startTime);
					while (!renderingFinished)
						Thread.sleep(getFramesPeriod() * 2);
					framesChronometer.tick();
				}
			}
		};
		Thread updateGUI = new Thread(task);
		updateGUI.setDaemon(true);
		updateGUI.start();
	}

	private synchronized void update(final Group root) {
		root.getChildren().clear();
		root.getChildren().add(getEcosystemView().toNode());
		root.getChildren().add(foreground);
		root.getChildren().add(getStatusInfo());
	}

	private void bindViewportSizeToWindowSize(final Scene scene, final Viewport viewport) {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				viewport.setSizeSC(Vector.cartesian(newSceneWidth.doubleValue(), viewport.getSizeSC().y));
				updateForeground();
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				viewport.setSizeSC(Vector.cartesian(viewport.getSizeSC().x, newSceneHeight.doubleValue()));
				updateForeground();
			}
		});
	}

	void waitFor(int time, long since) {
		long timeTaken = System.currentTimeMillis() - since;
		long waitTime = Math.max(time - timeTaken, 1);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
		}
	}

	private synchronized void updateForeground() {
		foreground = createForeground();
	}

	private Node createForeground() {
		Vector sizeSC = getViewport().getSizeSC();
		double minScreenSize = Math.min(sizeSC.x, sizeSC.y);
		double maxScreenSize = Math.max(sizeSC.x, sizeSC.y);
		Rectangle black = new Rectangle(-10, -10, maxScreenSize + 20, maxScreenSize + 20);
		Circle hole = new Circle(sizeSC.x / 2, sizeSC.y / 2, minScreenSize / 2.03);
		Shape microscope = Shape.subtract(black, hole);
		microscope.setEffect(new BoxBlur(5, 5, 1));
		return microscope;
	}

	private int getFramesPeriod() {
		if (state.getLight() == Light.OFF)
			return FRAMES_PERIOD_WITH_LIGHT_OFF;
		else
			return FRAMES_PERIOD_WITH_LIGHT_ON;
	}

	private EventHandler<? super KeyEvent> createKeyboardHandler() {
		return new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.RIGHT)
					moveViewport(PAN_SPEED, 0, keyEvent);
				else if (keyEvent.getCode() == KeyCode.LEFT)
					moveViewport(-PAN_SPEED, 0, keyEvent);
				else if (keyEvent.getCode() == KeyCode.UP)
					moveViewport(0, -PAN_SPEED, keyEvent);
				else if (keyEvent.getCode() == KeyCode.DOWN)
					moveViewport(0, PAN_SPEED, keyEvent);
				else if (keyEvent.getCode() == KeyCode.P)
					state.togglePause();
				else if (keyEvent.getCode() == KeyCode.S)
					state.shiftSpeed();
				else if (keyEvent.getCode() == KeyCode.L) {
					state.toggleLight();
					getEcosystemView().setLight(state.getLight());
				} else if (keyEvent.getCode() == KeyCode.I) {
					state.toggleInfrared();
					getEcosystemView().setLight(state.getLight());
				}
			}
		};
	}

	private EventHandler<MouseEvent> createMouseHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());

				getViewport().flyToTargetSC(clickedPoint);

				Vector clickedPointEC = getViewport().toEC(clickedPoint);
				Narjillo narjillo = locator.findNarjilloNear(clickedPointEC);

				if (event.getClickCount() == 1) {
					if (isLocked()) {
						if (narjillo == null)
							unlock();
						else
							lockOn(narjillo);
					}
					getViewport().flyToNextZoomCloseupLevel();
				}

				if (event.getClickCount() >= 2) {
					if (narjillo == null)
						getViewport().flyToNextZoomCloseupLevel();
					else {
						lockOn(narjillo);
						getViewport().flyToMaxZoomCloseupLevel();
					}
				}

				if (event.getClickCount() == 3)
					printOutDNA(clickedPoint);
			}

			private void printOutDNA(Vector clickedPoint) {
				Vector clickedPointEC = getViewport().toEC(clickedPoint);
				Narjillo narjillo = locator.findNarjilloNear(clickedPointEC);
				if (narjillo == null)
					return;
				DNA dna = narjillo.getDNA();
				System.out.println("Isolating: " + JSON.toJson(dna, DNA.class));
			}
		};
	}

	private EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0) {
					getViewport().zoomOut();
					if (getViewport().isZoomedOutCompletely())
						unlock();
				} else
					getViewport().zoomIn();
			}
		};
	}

	private synchronized Node getStatusInfo() {
		return dataView.toNode(state.getSpeed(), framesChronometer, isLocked());
	}

	private void moveViewport(long velocityX, long velocityY, KeyEvent event) {
		lockedOn = Thing.NULL;
		getViewport().moveBy(Vector.cartesian(velocityX, velocityY));
		event.consume();
	};

	public static void main(String... args) throws Exception {
		PetriDish.programArguments = args;
		launch(args);
	}

	private void lockOn(Thing narjillo) {
		lockedOn = narjillo;
	}

	private Thing unlock() {
		return lockedOn = Thing.NULL;
	}

	private boolean isLocked() {
		return lockedOn != Thing.NULL;
	}
}
