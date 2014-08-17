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

	public void updateAngleToParent(double targetPercentOfAmplitude, double skewing) {
		previousPosition = getSegment();

		double targetAngleToParent = getNerve().tick(targetPercentOfAmplitude);
		
		double angleToParent = calculateAngleToParent(targetAngleToParent, skewing);
		
		setAngleToParent(angleToParent);

		resetForcedBend();

		for (Organ child : getChildren())
			child.updateAngleToParent(targetAngleToParent, skewing);
	}

	public void recordForce(ForceField forceField) {
		forceField.record(previousPosition, this);

		for (Organ child : getChildren())
			child.recordForce(forceField);
	}

	protected abstract double calculateAngleToParent(double targetAngle, double skewing);

	Nerve getNerve() {
		return nerve;
	}

	public BodySegment sproutOrgan(int length, int thickness, ColorByte hue, int delay, int angleToParentAtRest, int amplitude) {
		return addChild(new BodySegment(length, thickness, hue, this, new DelayNerve(delay), angleToParentAtRest, amplitude));
	}

	// TODO: remove and consider pushing down the sproutOrgan group of methods
	Organ sproutOrgan(Nerve nerve) {
		return addChild(new BodySegment(nerve));
	}

	private BodySegment addChild(BodySegment child) {
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