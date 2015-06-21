package org.nusco.narjillos.ecosystem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;
import org.nusco.narjillos.genomics.GenePool;

/**
 * Base class for all the environments where Narjillos can live.
 */
public abstract class Culture {

	public static int numberOfBackgroundThreads = Runtime.getRuntime().availableProcessors();

	private final long size;
	private final List<EnvironmentEventListener> eventListeners = new LinkedList<>();
	private final ExecutorService executorService;
	private final Atmosphere atmosphere = new Atmosphere();
	
	/** Counter used by the ThreadFactory to name threads. */
	private final AtomicInteger tickWorkerCounter = new AtomicInteger(1);

	public Culture(long size) {
		this.size = size;
		ThreadFactory tickWorkerFactory = (Runnable r) -> {
			Thread result = new Thread(r, "tick-worker-" + tickWorkerCounter.getAndIncrement());
			result.setPriority(Thread.currentThread().getPriority());
			return result;
		};
		executorService = Executors.newFixedThreadPool(numberOfBackgroundThreads, tickWorkerFactory);
	}

	public abstract Set<Thing> getThings(String label);

	public abstract int getNumberOfNarjillos();

	public abstract int getNumberOfEggs();

	public abstract int getNumberOfFoodPieces();

	/** Runs one simulation tick */
	public void tick(GenePool genePool, RanGen ranGen) {
		if (isShuttingDown())
			return;		// we're leaving, apparently

		tickThings(genePool, ranGen);

		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	public synchronized void terminate() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public long getSize() {
		return size;
	}

	public void addEventListener(EnvironmentEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public Atmosphere getAtmosphere() {
		return atmosphere;
	}
	
	protected abstract Set<Thing> getCollisions(Segment movement);

	protected abstract void tickThings(GenePool genePool, RanGen ranGen);

	protected boolean isShuttingDown() {
		return executorService.isShutdown();
	}

	protected Map<Narjillo, Future<Set<Thing>>> tickNarjillos(Set<Narjillo> narjillos) {
		Map<Narjillo, Future<Set<Thing>>> result = new LinkedHashMap<>();
		for (final Narjillo narjillo : narjillos) {
			result.put(narjillo, executorService.submit(() -> {
				Segment movement = narjillo.tick();
				return getCollisions(movement);
			}));
		}
		return result;
	}

	protected final void notifyThingAdded(Thing thing) {
		eventListeners.stream().forEach((ecosystemEvent) -> {
			ecosystemEvent.thingAdded(thing);
		});
	}

	protected final void notifyThingRemoved(Thing thing) {
		eventListeners.stream().forEach((ecosystemEvent) -> {
			ecosystemEvent.thingRemoved(thing);
		});
	}
}
