package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.GenePool;

public class IsolationCulture extends Culture {

	private static final Set<Thing> EMPTY_SET = Collections.unmodifiableSet(new LinkedHashSet<Thing>());

	private Set<Narjillo> narjillos = new LinkedHashSet<>();

	public IsolationCulture(long size, RanGen ranGen) {
		super(size);
	}

	@Override
	public Set<Thing> getThings(String label) {
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
		tickNarjillos(narjillos);
	}

	@Override
	protected Set<Thing> getCollisions(Segment movement) {
		return EMPTY_SET;
	}

	public void updateSpecimen(Narjillo narjillo) {
		if (!narjillos.isEmpty()) {
			Narjillo existingNarjillo = narjillos.iterator().next();
			narjillos.remove(existingNarjillo);
			notifyThingRemoved(existingNarjillo);
		}
		
		narjillos.add(narjillo);
		notifyThingAdded(narjillo);
	}

	public Narjillo getNarjillo() {
		return narjillos.iterator().next();
	}
}
