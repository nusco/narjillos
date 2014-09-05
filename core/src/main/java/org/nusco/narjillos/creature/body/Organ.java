package org.nusco.narjillos.creature.body;

import java.util.ArrayList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Connects with other BodyParts in a tree. Also moves, thanks to the calculateAngleToParent()
 * abstract method.
 */
public strictfp abstract class Organ extends BodyPart {

	private final Nerve nerve;
	private transient Organ parent;
	private final List<Organ> children = new ArrayList<>();
	private double angleToParent = 0;
	
	protected Organ(int length, int thickness, ColorByte color, Organ parent, Nerve nerve) {
		super(length, thickness, color);
		this.nerve = nerve;
		setParent(parent);
	}

	public final void setParent(Organ parent) {
		this.parent = parent;
	}

	protected final double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
		recursivelyCalculateCachedFields();
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

	public void recursivelyUpdateAngleToParent(double percentOfAmplitude, double angleToTarget) {
		double processedPercentOfAmplitude = getNerve().tick(percentOfAmplitude);
		
		double newAngleToParent = calculateNewAngleToParent(processedPercentOfAmplitude, angleToTarget);
		setAngleToParent(newAngleToParent);

		for (Organ child : getChildren())
			child.recursivelyUpdateAngleToParent(processedPercentOfAmplitude, angleToTarget);
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