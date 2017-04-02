package org.nusco.narjillos.experiment.environment;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNALog;

/**
 * An environment that isolates a single narjillo at a time.
 */
public class IsolationEnvironment extends Environment {

	private final List<Narjillo> narjillos = new LinkedList<>();

	private double targetAngle = 0;

	public IsolationEnvironment(long size) {
		super(size);
	}

	@Override
	public synchronized List<Thing> getThings(String label) {
		if (label.equals(Narjillo.LABEL) || label.equals(""))
			return new LinkedList<>(narjillos);
		return Collections.emptyList();
	}

	@Override
	public long getCount(String label) {
		if (label.equals(Narjillo.LABEL))
			return 1;

		return 0;
	}

	@Override
	protected void tickThings(DNALog dnaLog, NumGen numGen) {
		getNarjillo().tick();
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

	public synchronized Narjillo getNarjillo() {
		return narjillos.iterator().next();
	}

	public synchronized void rotateTarget() {
		targetAngle = (targetAngle + 90) % 360;
	}

	public synchronized void setTarget() {
		getNarjillo().setTarget(getNarjillo().getPosition().plus(Vector.polar(targetAngle, 1_000_000.0)));
	}
}
