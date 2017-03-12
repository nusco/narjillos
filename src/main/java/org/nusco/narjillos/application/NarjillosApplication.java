package org.nusco.narjillos.application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.application.Application;
import javafx.application.Platform;
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
	// them. So we can skip synchronization altogether.

	private Dish dish;

	private StoppableThread modelThread;

	private StoppableThread viewThread;

	private Viewport viewport;

	private Locator locator;

	private ThingTracker tracker;

	// TODO: making this volatile means nothing, since it's a final array. Fix.
	private volatile boolean[] isModelInitialized = new boolean[] { false };

	private volatile boolean mainApplicationStopped = false;

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

	protected abstract void startSupportThreads();

	protected abstract void registerInteractionHandlers(Scene scene);

	// Remember: we need to initialize the random number generator inside this
	// thread, because it will complain if it is called from multiple threads.
	protected abstract StoppableThread createModelThread(String[] arguments, boolean[] isModelInitialized);

	protected abstract StoppableThread createViewThread(Group root);

	protected abstract String getName();

	protected static void setProgramArguments(String[] programArguments) {
		NarjillosApplication.programArguments = programArguments;
	}

	void waitFor(int time, long since) {
		long timeTaken = System.currentTimeMillis() - since;
		long waitTime = Math.max(time - timeTaken, 1);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
		}
	}

	boolean isMainApplicationStopped() {
		return mainApplicationStopped;
	}

	Viewport getViewport() {
		return viewport;
	}

	ThingTracker getTracker() {
		return tracker;
	}

	synchronized Dish getDish() {
		return dish;
	}

	synchronized void setDish(Dish dish) {
		this.dish = dish;
	}

	Environment getEcosystem() {
		return getDish().getEnvironment();
	}

	boolean tick() {
		return getDish().tick();
	}

	String getDishStatistics() {
		return getDish().getStatistics();
	}

	String getEnvironmentStatistics() {
		Environment environment = getDish().getEnvironment();
		return "Narj: " + environment.getNumberOfNarjillos()
			+ " / Eggs: " + environment.getNumberOfEggs()
			+ " / Food: " + environment.getNumberOfFoodPellets();
	}

	boolean isBusy() {
		return getDish().isBusy();
	}

	void copyDNAToClipboard(Vector clickedPositionEC) {
		Narjillo narjillo = (Narjillo) getLocator().findNarjilloAt(clickedPositionEC);

		if (narjillo == null)
			return;

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(narjillo.getDNA().toString()), null);
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

	private static String[] getProgramArguments() {
		return programArguments;
	}

	private void startViewThread(final Group root) {
		viewThread = createViewThread(root);
		viewThread.setName("view thread");
		viewThread.start();
	}

	private void bindViewportSizeToWindowSize(final Scene scene, final Viewport viewport) {
		scene.widthProperty().addListener(
			(observableValue, oldSceneWidth, newSceneWidth) -> viewport.setSizeSC(Vector.cartesian(newSceneWidth.doubleValue(), viewport.getSizeSC().y)));
		scene.heightProperty().addListener(
			(observableValue, oldSceneHeight, newSceneHeight) -> viewport.setSizeSC(Vector.cartesian(viewport.getSizeSC().x, newSceneHeight.doubleValue())));
	}

	private Locator getLocator() {
		return locator;
	}
}