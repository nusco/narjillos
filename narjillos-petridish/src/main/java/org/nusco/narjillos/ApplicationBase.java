package org.nusco.narjillos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.nusco.narjillos.ecosystem.Environment;
import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.utilities.Locator;
import org.nusco.narjillos.utilities.ThingTracker;
import org.nusco.narjillos.utilities.Viewport;

public abstract class ApplicationBase extends Application {

	// These fields are all just visualization stuff - no data will
	// get corrupted if different threads see slightly outdated versions of
	// them. So we can avoid synchronization altogether.

	private Lab lab;

	protected Thread modelThread;
	protected Thread viewThread;

	private Viewport viewport;
	private Locator locator;
	private ThingTracker tracker;

	@Override
	public void start(Stage primaryStage) {
		FastMath.setUp();
		Platform.setImplicitExit(true);

		startModelThread(getProgramArguments());

		System.gc(); // minimize GC during the first stages on animation

		viewport = new Viewport(getLab().getEnvironment());
		locator = new Locator(getLab().getEnvironment());
		tracker = new ThingTracker(viewport, locator);

		final Group root = new Group();
		startViewThread(root);

		final Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);
		registerInteractionHandlers(scene);
		bindViewportSizeToWindowSize(scene, viewport);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Narjillos - " + getName());
		primaryStage.show();
	}

	@Override
	public void stop() {
		// exit threads cleanly to avoid rare exception
		// when exiting Java FX application
		modelThread.interrupt();
		viewThread.interrupt();
		while (modelThread.isAlive() || viewThread.isAlive())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		getLab().terminate();

		Platform.exit();
	}

	protected void waitFor(int time, long since) {
		long timeTaken = System.currentTimeMillis() - since;
		long waitTime = Math.max(time - timeTaken, 1);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
		}
	}

	private void startModelThread(final String[] arguments) {
		final boolean[] isModelInitialized = new boolean[] { false };

		modelThread = createModelThread(arguments, isModelInitialized);
		modelThread.start();
		while (!isModelInitialized[0])
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
	}

	private void startViewThread(final Group root) {
		viewThread = createViewThread(root);
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

	protected abstract void registerInteractionHandlers(Scene scene);

	protected abstract String[] getProgramArguments();

	// Remember: we need to initialize the random number generator inside this
	// thread, because it will complain if it is called from multiple threads.
	protected abstract Thread createModelThread(String[] arguments, boolean[] isModelInitialized);

	protected abstract Thread createViewThread(Group root);

	protected abstract String getName();

	protected Viewport getViewport() {
		return viewport;
	}

	protected Locator getLocator() {
		return locator;
	}

	protected ThingTracker getTracker() {
		return tracker;
	}

	protected synchronized Lab getLab() {
		return lab;
	}

	protected synchronized void setLab(Lab lab) {
		this.lab = lab;
	}

	protected Environment getEcosystem() {
		return getLab().getEnvironment();
	}

	protected boolean tick() {
		return getLab().tick();
	}

	protected String getPerformanceStatistics() {
		return getLab().getPerformanceStatistics();
	}

	protected String getEnvironmentStatistics() {
		return getLab().getEnvironment().getStatistics();
	}

	protected boolean isBusy() {
		return getLab().isBusy();
	}
}