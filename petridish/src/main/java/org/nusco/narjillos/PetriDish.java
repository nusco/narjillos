package org.nusco.narjillos;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.nusco.narjillos.utilities.ThingTracker;
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
	private Locator locator;
	private ThingTracker tracker;

	private Thread modelThread;
	private Thread viewThread;
	private volatile boolean stopThreads = false;
	
	@Override
	public void start(Stage primaryStage) {
		FastMath.setUp();
		Platform.setImplicitExit(true);
		
		startModelThread(programArguments);

		System.gc(); // minimize GC during the first stages on animation

		viewport = new Viewport(lab.getEcosystem());
		locator = new Locator(lab.getEcosystem());
		tracker = new ThingTracker(viewport, locator);
		
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
		final boolean[] isModelInitialized = new boolean[] { false };

		modelThread = new Thread() {
			@Override
			public void run() {
				// We need to initialize the lab inside this thread, because
				// the random number generator will complain if it is called
				// from different threads.
				lab = new Lab(arguments);

				isModelInitialized[0] = true;

				while (!stopThreads) {
					long startTime = System.currentTimeMillis();
					if (state.getSpeed() != Speed.PAUSED)
						if (!lab.tick())
							Platform.exit();
					waitFor(state.getSpeed().getTicksPeriod(), startTime);
				}
			}
		};

		modelThread.start();
		while (!isModelInitialized[0])
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
	}

	private void startViewThread(final Group root) {
		viewThread = new Thread() {
			private final Chronometer framesChronometer = new Chronometer();
			private volatile boolean renderingFinished = false;

			private final MicroscopeView foregroundView = new MicroscopeView(viewport);
			private final EcosystemView ecosystemView = new EcosystemView(lab.getEcosystem(), viewport, state);
			private StatusBarView statusBarView = new StatusBarView(lab);

			public void run() {
				while (!stopThreads) {
					long startTime = System.currentTimeMillis();
					renderingFinished = false;

					tracker.track();
					ecosystemView.tick();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (stopThreads)
								return;
							
							update(root);
							renderingFinished = true;
						}
					});

					waitFor(state.getFramesPeriod(), startTime);
					while (!renderingFinished && !stopThreads)
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
						}
					framesChronometer.tick();
				}
			};

			private void update(final Group root) {
				root.getChildren().clear();
				root.getChildren().add(ecosystemView.toNode());
				root.getChildren().add(foregroundView.toNode());

				Node statusInfo = statusBarView.toNode(state.getSpeed(), state.getEffects(), framesChronometer, tracker.isTracking(), lab.isSaving());
				root.getChildren().add(statusInfo);
			}
		};
		viewThread.start();
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
				else if (keyEvent.getCode() == KeyCode.E)
					state.toggleEffects();
			}

			private void panViewport(long velocityX, long velocityY, KeyEvent event) {
				tracker.stopTracking();
				viewport.moveBy(Vector.cartesian(velocityX, velocityY));
				event.consume();
			};
		};
	}

	private EventHandler<MouseEvent> createMouseHandler() {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Vector clickedPoint = Vector.cartesian(event.getSceneX(), event.getSceneY());
				viewport.flyToTargetSC(clickedPoint);

				Vector clickedPointEC = viewport.toEC(clickedPoint);

				if (event.getClickCount() == 1)
					tracker.focusAt(clickedPointEC);
				
				if (event.getClickCount() >= 2)
					tracker.startTrackingAt(clickedPointEC);

				if (event.getClickCount() == 3)
					copyIsolatedDNAToClipboard(clickedPoint);

				event.consume();
			}

			private void copyIsolatedDNAToClipboard(Vector clickedPoint) {
				Narjillo narjillo = locator.findNarjilloNear(viewport.toEC(clickedPoint), Locator.MAX_FIND_DISTANCE);

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
						tracker.stopTracking();
				}

				event.consume();
			}
		};
	}

	@Override
	public void stop() throws Exception {
		lab.terminate();
		
		// exit threads cleanly to avoid rare exception
		// when exiting Java FX application
		stopThreads = true;
		while (modelThread.isAlive());
		while (viewThread.isAlive());

		Platform.exit();
	}

	public static void main(String... args) throws Exception {
		PetriDish.programArguments = args;
		launch(args);
	}
}
