package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * Connects with other BodyParts in a tree. Also moves, thanks to the calculateAngleToParent()
 * abstract method.
 */
public abstract class Organ extends BodyPart {

	private final Nerve nerve;
	private final BodyPart parent;
	private final List<Organ> children = new LinkedList<>();
	public double cachedLongestPathToLeaf = -1;

	private volatile double angleToParent = 0;
	public double skewing = 0;
	
	protected Organ(int length, int thickness, ColorByte color, BodyPart parent, Nerve nerve) {
		super(length, thickness, color);
		this.nerve = nerve;
		this.parent = parent;
	}

	protected double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
		recursivelyRalculateCachedFields();  // TODO: this is done at least twice per tick. is that necessary?
	}

	protected void recursivelyRalculateCachedFields() {
		super.updateCaches();

		for (Organ child : getChildren())
			child.recursivelyRalculateCachedFields();
	}

	@Override
	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getStartPoint().plus(getVector().by(0.5));
	}

	protected final BodyPart getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public void recursivelyUpdateAngleToParent(double targetPercentOfAmplitude) {
		double targetAngleToParent = getNerve().tick(targetPercentOfAmplitude);
		
		double newAngleToParent = calculateNewAngleToParent(targetAngleToParent);
		setAngleToParent(newAngleToParent);

		for (Organ child : getChildren())
			child.recursivelyUpdateAngleToParent(targetAngleToParent);
	}

	protected abstract double calculateNewAngleToParent(double targetAngle);

	Nerve getNerve() {
		return nerve;
	}

	public Organ addChild(Organ child) {
		children.add(child);
		return child;
	}

	protected double getSkewing() {
		return skewing;
	}

	void skew(double angle) {
		skewing = skewing + angle;
		for (Organ child : getChildren()) {
			child.resetSkewing();
			child.skew(skewing);
		}
	}

	public void resetSkewing() {
		skewing = 0;
	}

	public double calculateLongestPathToLeaf() {
		double longestRemainingPathToLeaf = 0;
		for (Organ child : children) {
			double longestPathThroughChild = child.calculateLongestPathToLeaf();
			if (longestPathThroughChild > longestRemainingPathToLeaf)
				longestRemainingPathToLeaf = longestPathThroughChild;
		}
		return getLength() + longestRemainingPathToLeaf;
	}
}