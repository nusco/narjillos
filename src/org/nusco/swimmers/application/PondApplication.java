package org.nusco.swimmers.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.Chronometer;
import org.nusco.swimmers.views.ChronometersView;
import org.nusco.swimmers.views.PondView;
import org.nusco.swimmers.views.Viewport;

public class PondApplication extends Application {

	private static final int TARGET_FRAMES_PER_SECOND = 25;
	private static final int FRAMES_PERIOD = 1000 / TARGET_FRAMES_PER_SECOND;
	private static final int DEFAULT_TARGET_TICKS_PER_SECOND = 25;
	protected static final long PAN_SPEED = 10;

	private final Chronometer ticksChronometer = new Chronometer();
	private final Chronometer framesChronometer = new Chronometer();
	private final ChronometersView chronometersView = new ChronometersView(framesChronometer, ticksChronometer);

	private PondView pondView;
	private Viewport viewport;
	private volatile int targetTicksPerSecond = DEFAULT_TARGET_TICKS_PER_SECOND;
	
	public static void main(String... args) {
		launch(args);
	}

	// TODO: do we need synchronized in the next two methods? and, does zooming
	// on the viewport need to be synchronized? And what about other methods
	// in this class?
	private synchronized void setPondView(PondView pondView) {
		this.pondView = pondView;
		this.viewport = pondView.getViewport();
	}

	private synchronized PondView getPondView() {
		return pondView;
	}

	@Override
	public void start(final Stage primaryStage) throws InterruptedException {
		final Group root = new Group();

		setUpNewPond();

		showRoot(root);

		startModelUpdateThread();
		startViewUpdateThread(root);

		final Viewport viewport = getPondView().getViewport();
		final Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);

		scene.setOnMouseClicked(createMouseEvent());
		scene.setOnScroll(createMouseScrollHandler());
		scene.setOnKeyPressed(createKeyboardHandler());
		addResizeListeners(scene, viewport);
		
		primaryStage.setTitle("Narjillos");
		primaryStage.setScene(scene);
		primaryStage.show();
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
			}
		};
	}

	private EventHandler<ScrollEvent> createMouseScrollHandler() {
		return new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0)
					viewport.zoomOut();
				else
					viewport.zoomIn();
			}
		};
	}

	private EventHandler<MouseEvent> createMouseEvent() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				handleMouse(event);
			}

			private synchronized void handleMouse(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					if (event.getClickCount() == 2)
						viewport.setCenterSC(Vector.cartesian(event.getSceneX(), event.getSceneY()));
					else {
						if (targetTicksPerSecond == 1000)
							targetTicksPerSecond = DEFAULT_TARGET_TICKS_PER_SECOND;
						else
							targetTicksPerSecond = 1000;
					}
				}
				if (event.getButton() == MouseButton.SECONDARY) {
					viewport.zoomToFit();
				}
			}
		};
	}

	private void addResizeListeners(final Scene scene, final Viewport viewport) {
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        viewport.setSizeSC(Vector.cartesian(newSceneWidth.doubleValue(), viewport.getSizeSC().y));
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        viewport.setSizeSC(Vector.cartesian(viewport.getSizeSC().x, newSceneHeight.doubleValue()));
		    }
		});
	}
	
	private void startViewUpdateThread(final Group root) {
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
					waitForAtLeast(FRAMES_PERIOD, startTime);
					while (!renderingFinished)
						Thread.sleep(FRAMES_PERIOD * 2);
					framesChronometer.tick();
				}
			}
		};
		Thread updateGUI = new Thread(task);
		updateGUI.setDaemon(true);
		updateGUI.start();
	}

	private int getTicksPeriod() {
		return 1000 / targetTicksPerSecond;
	}

	private void startModelUpdateThread() {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					long startTime = System.currentTimeMillis();
					tick();
					waitForAtLeast(getTicksPeriod(), startTime);
					ticksChronometer.tick();
				}
			}
		};
		updateThread.setDaemon(true);
		updateThread.start();
	}

	private synchronized void moveViewport(long velocityX, long velocityY, KeyEvent event) {
		viewport.moveBy(Vector.cartesian(velocityX, velocityY));
		event.consume();
	};

	private synchronized void tick() {
		getPondView().tick();
	}

	private synchronized void setUpNewPond() {
		setPondView(createNewPondView());
	}

	private synchronized PondView createNewPondView() {
		return new PondView(new Cosmos());
	}

	private synchronized void showRoot(final Group root) {
		root.getChildren().clear();
		showPond(root);
		showChronometers(root);
	}

	private void showPond(final Group root) {
		getPondView().show(root);
	}

	private void showChronometers(Group root) {
		root.getChildren().add(chronometersView.toNode());
	}

	private void waitForAtLeast(int time, long since) {
		long timeTaken = System.currentTimeMillis() - since;
		long waitTime = Math.max(time - timeTaken, 1);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
		}
	}
}
