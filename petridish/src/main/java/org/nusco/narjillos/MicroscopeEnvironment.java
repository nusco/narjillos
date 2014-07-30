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

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.views.PondView;
import org.nusco.narjillos.views.Viewport;

abstract class MicroscopeEnvironment extends Application {

	private static final int FRAMES_PER_SECOND = 25;
	private static final int FRAMES_PERIOD = 1000 / FRAMES_PER_SECOND;

	protected static final int DEFAULT_TICKS_PER_SECOND = 25;
	private static final int TICKS_PERIOD = 1000 / DEFAULT_TICKS_PER_SECOND;

	private static final long PAN_SPEED = 200;

	// TODO
	// This sucks. Each subclass needs to remember to set up
	// these arguments in its launch() method. That's what you
	// get for using frameworks.
	// The launch() method takes arguments, so it's probably
	// possible to retrieve the args from JavaFX. Check how.
	protected static String[] programArguments = new String[0];

	private Pond pond;
	private PondView pondView;
	
    private Node foreground;

    private volatile boolean modelThreadIsRunning;

	@Override
	public void start(final Stage primaryStage) throws InterruptedException {
		final Group root = new Group();

		startModelThread(programArguments);
		waitUntilModelThreadIsRunning();
		
		updateForeground();
		startViewThread(root);

		showRoot(root);

		final Viewport viewport = getPondView().getViewport();
		final Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);

		scene.setOnKeyPressed(createKeyboardHandler());
		scene.setOnMouseClicked(createMouseEvent());
		scene.setOnScroll(createMouseScrollHandler());
		addListenersToResizeViewportWhenTheUserResizesTheWindow(scene, viewport);
		
		primaryStage.setTitle(getTitle());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	protected Pond getPond() {
		return pond;
	}

	protected synchronized Viewport getViewport() {
		return getPondView().getViewport();
	}

	private synchronized void setPond(Pond pond) {
		this.pond = pond;
		this.pondView = new PondView(pond);
	}

	private synchronized PondView getPondView() {
		return pondView;
	}
	
	private void startModelThread(final String[] programArguments) {
		Thread updateThread = new Thread() {
			@Override
			public void run() {
				setPond(createPond(programArguments));

				modelThreadIsRunning = true;
				
				while (true) {
					long startTime = System.currentTimeMillis();
					tick();
					waitForAtLeast(getTicksPeriod(), startTime);
					afterModelTick();
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
					afterViewTick();
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
					getPondView().toggleInfrared();
			}
		};
	}

	protected abstract String getTitle();
	protected abstract Pond createPond(String[] programArguments);
	protected abstract void afterModelTick();
	protected abstract void afterViewTick();
	protected abstract Node getEnvironmentSpecificOverlay();
	protected abstract EventHandler<? super ScrollEvent> createMouseScrollHandler();
	protected abstract EventHandler<? super MouseEvent> createMouseEvent();

	private synchronized void showRoot(final Group root) {
		root.getChildren().clear();
		root.getChildren().add(getPondView().toNode());

		root.getChildren().add(foreground);
		
		Node environmentSpecificOverlay = getEnvironmentSpecificOverlay();
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
		getPond().tick();
		getPondView().tick();
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

	protected int getTicksPeriod() {
		return TICKS_PERIOD;
	}

	private int getFramesPeriod() {
		return FRAMES_PERIOD;
	}

	private void moveViewport(long velocityX, long velocityY, KeyEvent event) {
		getViewport().moveBy(Vector.cartesian(velocityX, velocityY));
		event.consume();
	};
}
