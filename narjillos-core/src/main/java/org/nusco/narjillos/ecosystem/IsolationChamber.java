package org.nusco.narjillos.ecosystem;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

/**
 * The place that Narjillos call "home".
 */
public class IsolationChamber extends Environment {

	private static final Set<Thing> EMPTY_SET = Collections.unmodifiableSet(new LinkedHashSet<Thing>());

	private Set<Thing> narjillos = new LinkedHashSet<Thing>();

	public IsolationChamber(long size) {
		super(size);
	}

	@Override
	public Set<Thing> getThings(String label) {
		if (!label.equals("narjillo"))
			return EMPTY_SET;
		return narjillos ;
	}

	@Override
	public void tick(GenePool genePool, RanGen ranGen) {
		// TODO Auto-generated method stub
	}

	@Override
	protected Set<Thing> getCollisions(Segment movement) {
		return EMPTY_SET;
	}

	@Override
	public String getStatistics() {
		return "TODO: statistics in IsolationEnvironment";
	}
}
