package org.nusco.narjillos.ecosystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.core.utilities.VisualDebugger;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;
import org.nusco.narjillos.genomics.GenePool;

/**
 * Base class for all the environments where Narjillos can live.
 */
public abstract class Culture {

	private final long size;
	private Atmosphere atmosphere;

	private final transient List<CultureEventListener> eventListeners = new LinkedList<>();

	public Culture(long size) {
		this.size = size;
		this.atmosphere = new Atmosphere();
	}

	public abstract Set<Thing> getThings(String label);

	public abstract int getNumberOfNarjillos();

	public abstract int getNumberOfEggs();

	public abstract int getNumberOfFoodPieces();

	/** Runs one simulation tick */
	public void tick(GenePool genePool, RanGen ranGen) {
		tickThings(genePool, ranGen);

		if (VisualDebugger.DEBUG)
			VisualDebugger.clear();
	}

	public long getSize() {
		return size;
	}

	public void addEventListener(CultureEventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public Atmosphere getAtmosphere() {
		return atmosphere;
	}

	public void setAtmosphere(Atmosphere atmosphere) {
		this.atmosphere = atmosphere;
	}

	protected abstract void tickThings(GenePool genePool, RanGen ranGen);

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
