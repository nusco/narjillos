package org.nusco.narjillos.experiment.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.genomics.DNALog;

/**
 * Base class for all the environments where Narjillos can live.
 */
public abstract class Environment {

	private final long size;
	private Atmosphere atmosphere;

	private final transient List<EnvironmentEventListener> eventListeners = new LinkedList<>();

	public Environment(long size) {
		this.size = size;
		this.atmosphere = new Atmosphere();
	}

	public abstract Set<Thing> getThings(String label);

	public abstract int getNumberOfNarjillos();

	public abstract int getNumberOfEggs();

	public abstract int getNumberOfFoodPellets();

	/** Runs one simulation tick */
	public void tick(DNALog dnaLog, NumGen numGen) {
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

	public Atmosphere getAtmosphere() {
		return atmosphere;
	}

	public void setAtmosphere(Atmosphere atmosphere) {
		this.atmosphere = atmosphere;
	}

	protected abstract void tickThings(DNALog dnaLog, NumGen numGen);

	protected final void notifyThingAdded(Thing thing) {
		eventListeners.stream().forEach((ecosystemEvent) -> {
			ecosystemEvent.added(thing);
		});
	}

	protected final void notifyThingRemoved(Thing thing) {
		eventListeners.stream().forEach((ecosystemEvent) -> {
			ecosystemEvent.removed(thing);
		});
	}
}
