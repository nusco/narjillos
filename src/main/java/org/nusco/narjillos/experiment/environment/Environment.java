package org.nusco.narjillos.experiment.environment;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.genomics.DNALog;

/**
 * Base class for all the environments where Narjillos can live.
 */
public abstract class Environment {

	private final long size;

	private final transient List<EnvironmentEventListener> eventListeners = new LinkedList<>();

	Environment(long size) {
		this.size = size;
	}

	public abstract List<Thing> getAll(String label);

	public abstract long getCount(String label);

	/**
	 * Runs one simulation tick
	 */
	public final void tick(DNALog dnaLog, NumGen numGen) {
		tickThings(dnaLog, numGen);

		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	public long getSize() {
		return size;
	}

	public void addEventListener(EnvironmentEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	protected abstract void tickThings(DNALog dnaLog, NumGen numGen);

	protected final void notifyThingAdded(Thing thing) {
		eventListeners.forEach(eventListener -> eventListener.added(thing));
	}

	protected final void notifyThingRemoved(Thing thing) {
		eventListeners.forEach(eventListener -> eventListener.removed(thing));
	}
}
