package org.nusco.narjillos.experiment.environment;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.GenePool;

/**
 * An environment that isolates a single narjillo at a time.
 */
public class IsolationEnvironment extends Environment {

	private static final Set<Thing> EMPTY_SET = Collections.unmodifiableSet(new LinkedHashSet<Thing>());

	private Set<Narjillo> narjillos = new LinkedHashSet<>();

	public IsolationEnvironment(long size, RanGen ranGen) {
		super(size);
	}

	@Override
	public synchronized Set<Thing> getThings(String label) {
		if (label.equals("narjillo") || label.equals(""))
			return new LinkedHashSet<Thing>(narjillos);
		return EMPTY_SET;
	}

	@Override
	public int getNumberOfNarjillos() {
		return 1;
	}

	@Override
	public int getNumberOfEggs() {
		return 0;
	}

	@Override
	public int getNumberOfFoodPieces() {
		return 0;
	}

	@Override
	protected void tickThings(GenePool genePool, RanGen ranGen) {
		getSpecimen().tick(getAtmosphere());
	}

	public synchronized void updateSpecimen(Narjillo narjillo) {
		if (!narjillos.isEmpty()) {
			Narjillo existingNarjillo = narjillos.iterator().next();
			narjillos.remove(existingNarjillo);
			notifyThingRemoved(existingNarjillo);
		}
		
		narjillos.add(narjillo);
		notifyThingAdded(narjillo);
	}

	public synchronized Narjillo getSpecimen() {
		return narjillos.iterator().next();
	}
}
