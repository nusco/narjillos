package org.nusco.narjillos.creature.body;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * A BodyPart inside a body. It's connected to other BodyParts in a tree structure,
 * and it can move via the tick() method. It leaves the details of movement to its
 * subclasses via the abstract calculateAngleToParent() method.
 */
public abstract class Organ extends BodyPart {

	private final Nerve nerve;
	private final BodyPart parent;
	private final List<Organ> children = new LinkedList<>();

	private double angleToParent = 0;
	public double forcedBend = 0;
	
	protected Organ(int length, int thickness, ColorByte hue, BodyPart parent, Nerve nerve) {
		super(length, thickness, hue);
		this.nerve = nerve;
		this.parent = parent;
	}

	protected double getAngleToParent() {
		return angleToParent;
	}

	protected final void setAngleToParent(double angleToParent) {
		this.angleToParent = angleToParent;
		resetAllCaches();
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

	public void tick(double targetAngleToParent, double skewing, ForceField forceField) {
		Segment segmentBeforeMovement = getSegment();

		double processedTargetAngleToParent = getNerve().tick(targetAngleToParent);
		
		double angleToParent = calculateAngleToParent(processedTargetAngleToParent, skewing, forceField);
		setAngleToParent(angleToParent);

		resetForcedBend();

		forceField.record(segmentBeforeMovement, this);
		tickChildren(processedTargetAngleToParent, skewing, forceField);
	}

	protected abstract double calculateAngleToParent(double targetAngle, double skewing, ForceField forceField);

	private void tickChildren(double targetAngle, double skewing, ForceField forceField) {
		for (Organ child : getChildren())
			child.tick(targetAngle, skewing, forceField);
	}

	Nerve getNerve() {
		return nerve;
	}

	public Organ sproutOrgan(int length, int thickness, ColorByte hue, int delay, int angleToParentAtRest) {
		return addChild(new BodySegment(length, thickness, hue, new DelayNerve(delay), angleToParentAtRest, this));
	}

	// TODO: remove
	Organ sproutOrgan(Nerve nerve) {
		return addChild(new BodySegment(nerve));
	}

	private Organ addChild(Organ child) {
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