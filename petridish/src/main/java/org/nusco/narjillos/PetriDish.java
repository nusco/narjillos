package org.nusco.narjillos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.utilities.Locator;
import org.nusco.narjillos.utilities.PetriDishState;
import org.nusco.narjillos.utilities.Speed;
import org.nusco.narjillos.utilities.Viewport;
import org.nusco.narjillos.views.EcosystemView;
import org.nusco.narjillos.views.MicroscopeView;
import org.nusco.narjillos.views.StatusBarView;

/**
 * The main JavaFX Application class. It binds model and view together, and also
 * manages the user interface.
 */
public class PetriDish extends Application {

	private static final long PAN_SPEED = 200;

	private static String[] programArguments = new String[0];

	private Lab lab;

	// These fields are all just visualization stuff - no data will
	// get corrupted if different threads see slightly outdated versions of
	// them. So we can avoid synchronization altogether.
	private PetriDishState state = new PetriDishState();
	private Viewport viewport;

	@Override
	public void start(Stage primaryStage) {
		FastMath.setUp();

		startModelThread(programArguments);

		System.gc(); // minimize GC during the first stages on animation

		viewport = new Viewport(lab.getEcosystem());

		final Group root = new Group();
		startViewThread(root);

		Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);
		scene.setOnKeyPressed(createKeyboardHandler());
		scene.setOnMouseClicked(createMouseHandler());
		scene.setOnScroll(createMouseScrollHandler());
		bindViewportSizeToWindowSize(scene, viewport);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Narjillos - Petri Dish");
		primaryStage.show();
	}

	private void startModelThread(final String[] arguments) {
		final boolean[] isInitializationDone = new boolean[] { false };

		Thread modelThread = new Thread() {
			@Override
			public void run() {
				// We need to initialize the lab inside this thread, because
				// the random number generator will complain if it is called
				// from different threads.
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
			private final Chronometer framesChronometer = new Chronometer();
			private volatile boolean renderingFinished = false;

			private final MicroscopeView foregroundView = new MicroscopeView(viewport);
			private final EcosystemView ecosystemView = new EcosystemView(lab.getEcosystem(), viewport, state);
			private StatusBarView statusBarView = new StatusBarView(lab);

			@Override
			public Void call() throws Exception {
				while (true) {
					long startTime = System.currentTimeMillis();
					renderingFinished = false;

					if (state.isLocked()) {
						Narjillo narjillo = (Narjillo) state.getLockedOn();
						if (narjillo.isDead())
							state.unlock();
						else
							viewport.flyToTargetEC(narjillo.calculateCenterOfMass());
					}

					ecosystemView.tick();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							update(root);
							renderingFinished = true;
						}
					});
					waitFor(state.getFramesPeriod(), startTime);
					while (!renderingFinished)
						Thread.sleep(state.getFramesPeriod() * 2);
					framesChronometer.tick();
				}
			}

			private void update(final Group root) {
				root.getChildren().clear();
				root.getChildren().add(ecosystemView.toNode());
				root.getChildren().add(foregroundView.toNode());

				Node statusInfo = statusBarView.toNode(state.getSpeed(), state.getMotionBlur(), framesChronometer, state.isLocked(), lab.isSaving());
				root.getChildren().add(statusInfo);
			}
		};
		Thread updateGUI = new Thread(task);
		updateGUI.setDaemon(true);
		updateGUI.start();
	}

	private void bindViewportSizeToWindowSize(final Scene scene, final Viewport viewport) {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				viewport.setSizeSC(Vector.cartesian(newSceneWidth.doubleValue(), viewport.getSizeSC().y));
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				viewport.setSizeSC(Vector.cartesian(viewport.getSizeSC().x, newSceneHeight.doubleValue()));
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

	private EventHandler<? super KeyEvent> createKeyboardHandler() {
		return new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.RIGHT)
					panViewport(PAN_SPEED, 0, keyEvent);
				else if (keyEvent.getCode() == KeyCode.LEFT)
					panViewport(-PAN_SPEED, 0, keyEvent);
				else if (keyEvent.getCode() == KeyCode.UP)
					panViewport(0, -PAN_SPEED, keyEvent);
				else if (keyEvent.getCode() == KeyCode.DOWN)
					panViewport(0, PAN_SPEED, keyEvent);
				else if (keyEvent.getCode() == KeyCode.P)
					state.speedUp();
				else if (keyEvent.getCode() == KeyCode.O)
					state.speedDown();
				else if (keyEvent.getCode() == KeyCode.L)
					state.toggleLight();
				else if (keyEvent.getCode() == KeyCode.I)
					state.toggleInfrared();
				else if (keyEvent.getCode() == KeyCode.M)
					state.toggleMotionBlur();
			}

			private void panViewport(long velocityX, long velocityY, KeyEvent event) {
				state.unlock();
				viewport.moveBy(Vector.cartesian(velocityX, velocityY));
				event.consume();
			};
		};
	}

	private EventHandler<MouseEvent> createMouseHandler() {
		return new EventHandler<MouseEvent>() {
			private Locator locator = new Locator(lab.getEcosystem());

			public void handle(MouseEvent event) {
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				viewport.flyToTargetSC(clickedPoint);

				Vector clickedPointEC = viewport.toEC(clickedPoint);
				Narjillo narjillo = locator.findNarjilloNear(clickedPointEC);

				if (event.getClickCount() == 1) {
					if (state.isLocked()) {
						if (narjillo == null)
							state.unlock();
						else
							state.lockOn(narjillo);
					}
					viewport.flyToNextZoomCloseupLevel();
				}

				if (event.getClickCount() >= 2) {
					if (narjillo == null)
						viewport.flyToNextZoomCloseupLevel();
					else {
						state.lockOn(narjillo);
						viewport.flyToMaxZoomCloseupLevel();
					}
				}

				if (event.getClickCount() == 3)
					copyIsolatedDNAToClipboard(clickedPoint);

				event.consume();
			}

			private void copyIsolatedDNAToClipboard(Vector clickedPoint) {
				Narjillo narjillo = locator.findNarjilloNear(viewport.toEC(clickedPoint));

				if (narjillo == null)
					return;

				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(narjillo.getDNA().toString()), null);
			}
		};
	}

	private EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() <= 0)
					viewport.zoomIn();
				else {
					viewport.zoomOut();
					if (viewport.isZoomedOutCompletely())
						state.unlock();
				}

				event.consume();
			}
		};
	}

	public static void main(String... args) throws Exception {
		PetriDish.programArguments = args;
		launch(args);
	}
}
