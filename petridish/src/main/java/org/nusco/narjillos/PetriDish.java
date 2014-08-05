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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.views.DataView;
import org.nusco.narjillos.views.EcosystemView;
import org.nusco.narjillos.views.Viewport;

public class PetriDish extends Application {

	private static final int FRAMES_PER_SECOND = 25;
	private static final int FRAMES_PERIOD = 1000 / FRAMES_PER_SECOND;
	private static final int DEFAULT_TICKS_PER_SECOND = 25;
	private static final long PAN_SPEED = 200;

	private static String[] programArguments = new String[0];

	private Experiment experiment;
	private EcosystemView ecosystemView;
	
    private Node foreground;
	private final Chronometer framesChronometer = new Chronometer();
	private int targetTicksPerSecond = DEFAULT_TICKS_PER_SECOND;

	private volatile boolean modelThreadIsRunning;
	
	public PetriDish() {
	}

	@Override
	public void start(final Stage primaryStage) throws InterruptedException {
		final Group root = new Group();

		startModelThread(programArguments);
		waitUntilModelThreadIsRunning();
		
		updateForeground();
		startViewThread(root);

		showRoot(root);

		final Viewport viewport = getEcosystemView().getViewport();
		final Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);

		scene.setOnKeyPressed(createKeyboardHandler());
		scene.setOnMouseClicked(createMouseEvent());
		scene.setOnScroll(createMouseScrollHandler());
		addListenersToResizeViewportWhenTheUserResizesTheWindow(scene, viewport);
		
		primaryStage.setTitle("Narjillos - Petri Dish");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private Viewport getViewport() {
		return getEcosystemView().getViewport();
	}

	private Ecosystem getEcosystem() {
		return experiment.getEcosystem();
	}

	private synchronized EcosystemView getEcosystemView() {
		return ecosystemView;
	}
	
	private void startModelThread(final String[] experimentArguments) {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				experiment = new Experiment(experimentArguments);
				ecosystemView = new EcosystemView(getEcosystem());

				modelThreadIsRunning = true;
				
				while (true) {
					long startTime = System.currentTimeMillis();
					tick();
					waitForAtLeast(getTicksPeriod(), startTime);
				}
			}
		};
		updateThread.setDaemon(true);
		updateThread.start();
	}

	private void startViewThread(final Group root) {
		Task<Void> task = new Task<Void>() {
			private volatile boolean renderingFinished = false;

			@Override
			public Void call() throws Exception {
				while (true) {
					long startTime = System.currentTimeMillis();
					renderingFinished = false;

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							showRoot(root);
							renderingFinished = true;
						}
					});
					waitForAtLeast(getFramesPeriod(), startTime);
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
				else if (keyEvent.getCode() == KeyCode.ENTER)
					getEcosystemView().toggleInfrared();
			}
		};
	}

	private synchronized void showRoot(final Group root) {
		root.getChildren().clear();
		root.getChildren().add(getEcosystemView().toNode());

		root.getChildren().add(foreground);
		
		Node environmentSpecificOverlay = getChronometers();
		if (environmentSpecificOverlay != null)
			root.getChildren().add(environmentSpecificOverlay);
	}

	private void addListenersToResizeViewportWhenTheUserResizesTheWindow(final Scene scene, final Viewport viewport) {
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

	private void tick() {
		experiment.tick();
		getEcosystemView().tick();
	}

	void waitForAtLeast(int time, long since) {
		long timeTaken = System.currentTimeMillis() - since;
		long waitTime = Math.max(time - timeTaken, 1);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
		}
	}

	private void waitUntilModelThreadIsRunning() throws InterruptedException {
		while (!modelThreadIsRunning)
			Thread.sleep(10);
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
		return FRAMES_PERIOD;
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

	protected EventHandler<MouseEvent> createMouseEvent() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					toggleMaxSpeed();
					return;
				}
				
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				getViewport().flyToTargetSC(clickedPoint);
				
				if (event.getClickCount() > 1)
					getViewport().flyToNextZoomCloseupLevel();
			}
		};
	}

	private synchronized void toggleMaxSpeed() {
		if (targetTicksPerSecond == 1000)
			targetTicksPerSecond = DEFAULT_TICKS_PER_SECOND;
		else
			targetTicksPerSecond = 1000;
	}

	private synchronized int getTicksPeriod() {
		return 1000 / targetTicksPerSecond;
	}

	private synchronized Node getChronometers() {
		Color color = isRealTime() ? Color.LIGHTGREEN : Color.HOTPINK;
		return DataView.toNode(getPerformanceMessage() + "\n" + getStatisticsMessage(), color);
	}

	private String getStatisticsMessage() {
		return "NARJ: " + getEcosystem().getNumberOfNarjillos() + " / FOOD: " + getEcosystem().getNumberOfFoodPieces();
	}

	private String getPerformanceMessage() {
		String result = "FPS: " + framesChronometer.getTicksInLastSecond() +
						" / TPS: " + experiment.getTicksChronometer().getTicksInLastSecond() +
						" / TICKS: " + NumberFormat.format(experiment.getTicksChronometer().getTotalTicks());
		if (isRealTime())
			return result + " (realtime)";
		else
			return result + " (hi-speed)";
	}

	private boolean isRealTime() {
		return targetTicksPerSecond == DEFAULT_TICKS_PER_SECOND;
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
