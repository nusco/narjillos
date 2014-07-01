package org.nusco.swimmers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.views.PondView;

public class PondApplication extends Application {

	private static final int FRAMES_PER_SECOND = 25;
	private static final int TICKS_PER_SECOND = 25;
	private static final int FRAMES_PERIOD = 1000 / FRAMES_PER_SECOND;
	private static final int TICKS_PERIOD = 1000 / TICKS_PER_SECOND;

	private PondView pondView;

	public static void main(String... args) {
		launch(args);
	}

	private synchronized void setPondView(PondView pondView) {
		this.pondView = pondView;
	}

	private synchronized PondView getPondView() {
		return pondView;
	}

	@Override
	public void start(final Stage primaryStage) throws InterruptedException {
		final Group root = new Group();

		setUpNewPond();
		showPond(root);

		startModelUpdateThread();
		startViewUpdateThread(root);

		double viewSize = (double) getPondView().getViewSize();
		final Scene scene = new Scene(root, viewSize, viewSize);
		scene.setOnMouseClicked(createMouseEvent());
		primaryStage.setTitle("Swimmers");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private EventHandler<MouseEvent> createMouseEvent() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				handleMouse(event);
			}

			private synchronized void handleMouse(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY)
					getPondView().zoomIn(Vector.cartesian(event.getX(), event.getY()));
				else
					getPondView().zoomOut();
			}
		};
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
							showPond(root);
							renderingFinished = true;
						}
					});
					waitForAtLeast(FRAMES_PERIOD, startTime);
					while (!renderingFinished)
						Thread.sleep(FRAMES_PERIOD * 2);
				}
			}
		};
		Thread updateGUI = new Thread(task);
		updateGUI.setDaemon(true);
		updateGUI.start();
	}

	private void startModelUpdateThread() {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					long startTime = System.currentTimeMillis();
					tick();
					waitForAtLeast(TICKS_PERIOD, startTime);
				}
			}
		};
		updateThread.setDaemon(true);
		updateThread.start();
	}

	private synchronized void tick() {
		getPondView().tick();
	}

	private synchronized void setUpNewPond() {
		setPondView(createNewPondView());
	}

	private synchronized PondView createNewPondView() {
		return new PondView(new RandomPond());
	}

	private synchronized void showPond(final Group root) {
		getPondView().show(root);
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
