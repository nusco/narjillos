package org.nusco.narjillos.ecosystem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.GenePool;

public abstract class Culture {

	public static volatile int numberOfBackgroundThreads = Runtime.getRuntime().availableProcessors();

	private final long size;
	private final List<EnvironmentEventListener> eventListeners = new LinkedList<>();
	private volatile ExecutorService executorService;
	private volatile int tickWorkerCounter = 1;
	
	public Culture(long size) {
		this.size = size;
		ThreadFactory tickWorkerFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread result = new Thread(r, "tick worker " + tickWorkerCounter++);
				result.setPriority(Thread.currentThread().getPriority());
				return result;
			}
		};
		executorService = Executors.newFixedThreadPool(numberOfBackgroundThreads, tickWorkerFactory);
	}

	public long getSize() {
		return size;
	}

	public void tick(GenePool genePool, RanGen ranGen) {
		if (isShuttingDown())
			return; // we're leaving, apparently

		tickThings(genePool, ranGen);

		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	public void addEventListener(EnvironmentEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public abstract String getStatistics();

	public synchronized void terminate() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract Set<Thing> getThings(String label);

	protected boolean isShuttingDown() {
		return executorService.isShutdown();
	}

	protected abstract void tickThings(GenePool genePool, RanGen ranGen);

	protected Map<Narjillo, Future<Set<Thing>>> tickNarjillos(Set<Narjillo> narjillos) {
		Map<Narjillo, Future<Set<Thing>>> result = new LinkedHashMap<>();
		for (final Narjillo narjillo : narjillos) {
			result.put(narjillo, executorService.submit(new Callable<Set<Thing>>() {
				@Override
				public Set<Thing> call() throws Exception {
					Segment movement = narjillo.tick();
					return getCollisions(movement);
				}
			}));
		}
		return result;
	}

	protected abstract Set<Thing> getCollisions(Segment movement);

	protected final void notifyThingAdded(Thing thing) {
		for (EnvironmentEventListener ecosystemEvent : eventListeners)
			ecosystemEvent.thingAdded(thing);
	}

	protected final void notifyThingRemoved(Thing thing) {
		for (EnvironmentEventListener ecosystemEvent : eventListeners)
			ecosystemEvent.thingRemoved(thing);
	}
}