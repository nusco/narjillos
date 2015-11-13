package org.nusco.narjillos.application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.nusco.narjillos.application.utilities.Locator;
import org.nusco.narjillos.application.utilities.StoppableThread;
import org.nusco.narjillos.application.utilities.ThingTracker;
import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.geometry.FastMath;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.Environment;

abstract class NarjillosApplication extends Application {

	private static String[] programArguments = new String[0];

	// These fields are all just visualization stuff - no data will
	// get corrupted if different threads see slightly outdated versions of
	// them. So we can avoid synchronization altogether.

	private Dish dish;

	protected StoppableThread modelThread;
	protected StoppableThread viewThread;

	private Viewport viewport;
	private Locator locator;
	private ThingTracker tracker;

	private volatile boolean[] isModelInitialized = new boolean[] { false };
	private volatile boolean mainApplicationStopped = false;

	protected static String[] getProgramArguments() {
		return programArguments;
	}

	protected static void setProgramArguments(String[] programArguments) {
		NarjillosApplication.programArguments = programArguments;
	}

	@Override
	public void start(Stage primaryStage) {
		FastMath.setUp();
		Platform.setImplicitExit(true);
		
		// Use a single thread to tick narjillos when running an app.
		// The idea is that parallel processing is essential for speed
		// when you run a command-line experiment - but when you run
		// with graphics, normal realtime speed is adequate, and you
		// want to save CPU cores for the graphics instead.
		Ecosystem.numberOfBackgroundThreads = 1;
		
		startModelThread(getProgramArguments());

		System.gc(); // minimize GC during the first stages on animation

		viewport = new Viewport(getDish().getEnvironment());
		locator = new Locator(getDish().getEnvironment());
		tracker = new ThingTracker(viewport, locator);

		final Group root = new Group();
		startViewThread(root);

		startSupportThreads();

		final Scene scene = new Scene(root, viewport.getSizeSC().x, viewport.getSizeSC().y);
		registerInteractionHandlers(scene);
		bindViewportSizeToWindowSize(scene, viewport);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Narjillos - " + getName());
		primaryStage.show();
	}

	protected abstract void startSupportThreads();

	@Override
	public void stop() {
		// exit threads cleanly to avoid rare exception
		// when exiting Java FX application
		mainApplicationStopped = true;
		modelThread.askToStop();
		viewThread.askToStop();
		while (modelThread.isAlive() || viewThread.isAlive())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

		getDish().terminate();

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

	protected boolean isMainApplicationStopped() {
		return mainApplicationStopped;
	}
	
	private void startModelThread(final String[] arguments) {
		modelThread = createModelThread(arguments, isModelInitialized);
		modelThread.setName("model thread");
		// Graphics are more important than simulation speed here
		// (the current simulation runs just fine at 25 ticks per
		// second on regular computers). So demote the priority
		// of the simulation.
		modelThread.setPriority(Thread.MIN_PRIORITY);	
		modelThread.start();
		while (!isModelInitialized[0])
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
		}
	}

	private void startViewThread(final Group root) {
		viewThread = createViewThread(root);
		viewThread.setName("view thread");
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

	// Remember: we need to initialize the random number generator inside this
	// thread, because it will complain if it is called from multiple threads.
	protected abstract StoppableThread createModelThread(String[] arguments, boolean[] isModelInitialized);

	protected abstract StoppableThread createViewThread(Group root);

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

	protected synchronized Dish getDish() {
		return dish;
	}

	protected synchronized void setDish(Dish dish) {
		this.dish = dish;
	}

	protected Environment getEcosystem() {
		return getDish().getEnvironment();
	}

	protected boolean tick() {
		return getDish().tick();
	}

	protected String getDishStatistics() {
		return getDish().getStatistics();
	}

	protected String getEnvironmentStatistics() {
		Environment environment = getDish().getEnvironment();
		return "Narj: " + environment.getNumberOfNarjillos()
				+ " / Eggs: " + environment.getNumberOfEggs()
				+ " / Food: " + environment.getNumberOfFoodPellets();
	}

	protected boolean isBusy() {
		return getDish().isBusy();
	}
	
	protected void copyDNAToClipboard(Vector clickedPositionEC) {
		Narjillo narjillo = (Narjillo) getLocator().findNarjilloAt(clickedPositionEC);

		if (narjillo == null)
			return;
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(narjillo.getDNA().toString()), null);
	}
}