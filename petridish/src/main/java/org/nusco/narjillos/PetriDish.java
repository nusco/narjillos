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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.views.DataView;
import org.nusco.narjillos.views.EcosystemView;
import org.nusco.narjillos.views.utilities.Light;
import org.nusco.narjillos.views.utilities.PetriDishState;
import org.nusco.narjillos.views.utilities.Speed;
import org.nusco.narjillos.views.utilities.Viewport;

public class PetriDish extends Application {

	private static final int FRAMES_PER_SECOND_WITH_LIGHT_ON = 25;
	private static final int FRAMES_PER_SECOND_WITH_LIGHT_OFF = 5;
	private static final int FRAMES_PERIOD_WITH_LIGHT_ON = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_ON;
	private static final int FRAMES_PERIOD_WITH_LIGHT_OFF = 1000 / FRAMES_PER_SECOND_WITH_LIGHT_OFF;
	private static final int TICKS_PER_SECOND = 25;
	private static final long PAN_SPEED = 200;

	private static String[] programArguments = new String[0];

	private Lab lab;
	private volatile EcosystemView ecosystemView;
	
    private Node foreground;
	private final PetriDishState state = new PetriDishState();
	private final Chronometer framesChronometer = new Chronometer();
	
	@Override
	public void start(final Stage primaryStage) {
		final Group root = new Group();

		startModelThread(programArguments);
		
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
		primaryStage.show();
	}

	private Viewport getViewport() {
		return getEcosystemView().getViewport();
	}

	private Ecosystem getEcosystem() {
		return lab.getEcosystem();
	}

	private synchronized EcosystemView getEcosystemView() {
		return ecosystemView;
	}
	
	private void startModelThread(final String[] arguments) {
		final boolean[] isModelThreadReady = new boolean[] { false };
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				lab = new Lab(arguments);
				ecosystemView = new EcosystemView(lab.getEcosystem());
				isModelThreadReady[0] = true;
				
				while (true) {
					long startTime = System.currentTimeMillis();
					if (state.getSpeed() != Speed.PAUSED)
						if (!lab.tick())
							return;
					waitUntilTimePassed(getTicksPeriod(), startTime);
				}
			}
		};
		updateThread.setDaemon(true);
		updateThread.start();
		while (!isModelThreadReady[0])
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

					ecosystemView.tick();
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							update(root);
							renderingFinished = true;
						}
					});
					waitUntilTimePassed(getFramesPeriod(), startTime);
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
					state.toggleSpeed();
				else if (keyEvent.getCode() == KeyCode.L) {
					state.toggleLight();
					getEcosystemView().setLight(state.getLight());
				}
				else if (keyEvent.getCode() == KeyCode.I) {
					state.toggleInfrared();
					getEcosystemView().setLight(state.getLight());
				}
			}
		};
	}

	private synchronized void update(final Group root) {
		root.getChildren().clear();
		root.getChildren().add(getEcosystemView().toNode());

		root.getChildren().add(foreground);
		
		Node environmentSpecificOverlay = getDataView();
		if (environmentSpecificOverlay != null)
			root.getChildren().add(environmentSpecificOverlay);
	}

	private void bindViewportSizeToWindowSize(final Scene scene, final Viewport viewport) {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	viewport.setSizeSC(Vector.cartesian(newSceneWidth.doubleValue(), viewport.getSizeSC().y));
		    	updateForeground();
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        viewport.setSizeSC(Vector.cartesian(viewport.getSizeSC().x, newSceneHeight.doubleValue()));
		        updateForeground();
		    }
		});
	}

	void waitUntilTimePassed(int time, long since) {
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

	protected EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0)
					getViewport().zoomOut();
				else
					getViewport().zoomIn();
			}
		};
	}

	protected EventHandler<MouseEvent> createMouseHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				
				getViewport().flyToTargetSC(clickedPoint);
				
				if (event.getClickCount() > 1)
					getViewport().flyToNextZoomCloseupLevel();

				if (event.getClickCount() > 2)
					printOutDNA(clickedPoint);
			}

			private void printOutDNA(Vector clickedPoint) {
				Vector clickedPointEC = getViewport().toEC(clickedPoint);
				DNA dna = getEcosystem().findNarjillo(clickedPointEC).getDNA();
				System.out.println("Isolating: " + JSON.toJson(dna, DNA.class));
			}
		};
	}

	private synchronized int getTicksPeriod() {
		if (state.getSpeed() == Speed.REALTIME)
			return 1000 / TICKS_PER_SECOND;

		// go as fast as possible
		return 1000 / Integer.MAX_VALUE;
	}

	private synchronized Node getDataView() {
		Color color = getDataViewColor();
		return DataView.toNode(getPerformanceMessage() + "\n" + getStatisticsMessage(), color);
	}

	private Color getDataViewColor() {
		switch(state.getSpeed()) {
		case HIGH:
			return Color.HOTPINK;
		case PAUSED:
			return Color.CYAN;
		case REALTIME:
			return Color.LIGHTGREEN;
		default:
			throw new RuntimeException("Unknown speed state: " + state.getSpeed());
		}
	}

	private String getStatisticsMessage() {
		return "NARJ: " + getEcosystem().getNumberOfNarjillos() + " / FOOD: " + getEcosystem().getNumberOfFoodPieces();
	}

	private String getPerformanceMessage() {
		return	"FPS: " + framesChronometer.getTicksInLastSecond() +
				" / TPS: " + lab.getTicksInLastSecond() +
				" / TICKS: " + NumberFormat.format(lab.getTotalTicks()) +
				" (" + getStateString() + ")";
	}

	private String getStateString() {
		return state.getSpeed().toString();
	}

	private void moveViewport(long velocityX, long velocityY, KeyEvent event) {
		getViewport().moveBy(Vector.cartesian(velocityX, velocityY));
		event.consume();
	};

	public static void main(String... args) throws Exception {
		PetriDish.programArguments = args;
		launch(args);
	}
}
