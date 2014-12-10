package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Adds the notion of the passing of time to the BodyPart hierarchy, embodied by
 * the tick() method. This means that it moves (see calculateAngleToParent())
 * and grows over time.
 */
public abstract class MovingOrgan extends ConnectedOrgan {

	private double angleToParent = 0;

	protected MovingOrgan(int adultLength, int adultThickness, ColorByte color, ConnectedOrgan parent, Nerve nerve) {
		super(adultLength, adultThickness, color, parent, nerve);
	}

	public void tick(double percentOfAmplitude, double angleToTarget) {
		double processedPercentOfAmplitude = getNerve().tick(percentOfAmplitude);

		recursivelyGrow();

		double newAngleToParent = calculateNewAngleToParent(processedPercentOfAmplitude, angleToTarget);
		updateAngleToParent(newAngleToParent);

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).tick(processedPercentOfAmplitude, angleToTarget);
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected final void updateAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;

		// Optimization: this is the only place where we update
		// the cache. Doing it more often would make the code
		// much simpler to follow, but also much slower. It's
		// important that all changes to Organ state pass by
		// here at some point.
		recursivelyCalculateCachedFields();
	}

	protected abstract double calculateNewAngleToParent(double targetAngle, double angleToTarget);

	private void recursivelyGrow() {
		grow();

		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).recursivelyGrow();
	}

	private void recursivelyCalculateCachedFields() {
		super.updateCaches();
	
		for (ConnectedOrgan child : getChildren())
			((MovingOrgan) child).recursivelyCalculateCachedFields();
	}
}