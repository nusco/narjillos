package org.nusco.narjillos.creature.body;

import java.util.ArrayList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Allows BodyParts to connect in a tree to form a body.
 * 
 * Also has a notion of the passing of time (embodied by the tick() method).
 * This means that it moves (see calculateAngleToParent()) and grows over time.
 */
public abstract class Organ extends BodyPart {

	private final Nerve nerve;
	private transient Organ parent;
	private final List<Organ> children = new ArrayList<>();
	private double angleToParent = 0;

	protected Organ(int adultLength, int adultThickness, ColorByte color, Organ parent, Nerve nerve) {
		super(adultLength, adultThickness, color);
		this.nerve = nerve;
		setParent(parent);
	}

	public final void setParent(Organ parent) {
		this.parent = parent;
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected void recursivelyCalculateCachedFields() {
		super.updateCaches();

		for (Organ child : getChildren())
			child.recursivelyCalculateCachedFields();
	}

	@Override
	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getStartPoint().plus(getVector().by(0.5));
	}

	public final Organ getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public void tick(double percentOfAmplitude, double angleToTarget) {
		double processedPercentOfAmplitude = getNerve().tick(percentOfAmplitude);

		recursivelyGrow();

		double newAngleToParent = calculateNewAngleToParent(processedPercentOfAmplitude, angleToTarget);
		updateAngleToParent(newAngleToParent);

		for (Organ child : getChildren())
			child.tick(processedPercentOfAmplitude, angleToTarget);
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

	public void recursivelyGrow() {
		grow();

		for (Organ child : getChildren())
			child.recursivelyGrow();
	}

	protected abstract double calculateNewAngleToParent(double targetAngle, double angleToTarget);

	Nerve getNerve() {
		return nerve;
	}

	public Organ addChild(Organ child) {
		children.add(child);
		return child;
	}

	protected abstract double getMetabolicRate();
}