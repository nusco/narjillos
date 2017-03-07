package org.nusco.narjillos.experiment.environment;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNALog;

/**
 * An environment that isolates a single narjillo at a time.
 */
public class IsolationEnvironment extends Environment {

	private static final Set<Thing> EMPTY_SET = Collections.unmodifiableSet(new LinkedHashSet<Thing>());

	private Set<Narjillo> narjillos = new LinkedHashSet<>();

	private double targetAngle = 0;

	public IsolationEnvironment(long size, NumGen numGen) {
		super(size);
	}

	@Override
	public synchronized Set<Thing> getThings(String label) {
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
	public int getNumberOfFoodPellets() {
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
