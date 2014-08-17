package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Segment;
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

	private double angleToParent = 0;
	public double forcedBend = 0;
	private Segment previousPosition;
	
	protected Organ(int length, int thickness, ColorByte color, BodyPart parent, Nerve nerve) {
		super(length, thickness, color);
		this.nerve = nerve;
		this.parent = parent;
	}

	protected synchronized double getAngleToParent() {
		return angleToParent;
	}

	protected synchronized final void setAngleToParent(double angleToParent) {
		previousPosition = getPositionInSpace();
		this.angleToParent = angleToParent;
		resetAllCaches();
	}

	@Override
	protected void resetAllCaches() {
		super.resetAllCaches();

		for (Organ child : getChildren())
			child.resetAllCaches();
	}

	protected Vector calculateStartPoint() {
		return getParent().getEndPoint();
	}

	@Override
	protected Vector calculateCenterOfMass() {
		return getParent().getCenterOfMass().plus(getVector().by(0.5));
	}

	protected final BodyPart getParent() {
		return parent;
	}

	public List<Organ> getChildren() {
		return children;
	}

	public void updateAngleToParent(double targetPercentOfAmplitude, double skewing) {
		double targetAngleToParent = getNerve().tick(targetPercentOfAmplitude);
		
		double newAngleToParent = calculateNewAngleToParent(targetAngleToParent, skewing);
		setAngleToParent(newAngleToParent);

		resetForcedBend();

		for (Organ child : getChildren())
			child.updateAngleToParent(targetAngleToParent, skewing);
	}

	public void calculateForces(ForceField forceField) {
		forceField.calculateForce(previousPosition, getPositionInSpace(), getMass());

		for (Organ child : getChildren())
			child.calculateForces(forceField);
	}

	protected abstract double calculateNewAngleToParent(double targetAngle, double skewing);

	Nerve getNerve() {
		return nerve;
	}

	public Organ addChild(Organ child) {
		children.add(child);
		return child;
	}

	protected double getForcedBend() {
		return forcedBend;
	}

	void forceBend(double bendAngle) {
		forcedBend = bendAngle;
	}
	
	private void resetForcedBend() {
		forcedBend = 0;
	}
}