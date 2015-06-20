package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.core.physics.Segment;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;

public class IsolationCulture extends Culture {

	private static final Set<Thing> EMPTY_SET = Collections.unmodifiableSet(new LinkedHashSet<Thing>());

	private final Set<Narjillo> narjillos = new LinkedHashSet<>();

	public IsolationCulture(long size, RanGen ranGen) {
		super(size);

		DNA dna = DNA.random(1, ranGen);
		Narjillo narjillo = new Narjillo(dna, Vector.cartesian(size, size).by(0.5), 180, Energy.INFINITE);
		narjillos.add(narjillo);
	}

	@Override
	public Set<Thing> getThings(String label) {
		if (label.equals("narjillo") || label.equals(""))
			return new LinkedHashSet<>(narjillos);
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
}
