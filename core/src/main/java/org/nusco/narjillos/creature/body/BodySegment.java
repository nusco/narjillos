package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

class BodySegment extends BodyPart {

	protected static final double MAX_ANGULAR_VELOCITY = 7;
	
	private final double angleToParentAtRest;

	public BodySegment(int length, int thickness, ColorByte hue, Nerve nerve, int angleToParentAtRest, BodyPart parent) {
		super(length, thickness, calculateColorMix(parent, hue), parent, nerve);
		this.angleToParentAtRest = angleToParentAtRest;
		setAngleToParent(angleToParentAtRest);
	}

	protected double calculateAngleToParent(double targetAngle, ForceField forceField) {
		double signedTargetAngle = targetAngle * getOrientationSign();
		double limitedTargetAngle = constrainToPhysicalLimits(signedTargetAngle);
		return getAngleToParent() + limitedTargetAngle;
	}

	protected double constrainToPhysicalLimits(double targetAngle) {
		double angularVelocity = targetAngle - getAngleToParent();
		if (Math.abs(angularVelocity) < MAX_ANGULAR_VELOCITY)
			return angularVelocity;
		return MAX_ANGULAR_VELOCITY * Math.signum(angularVelocity);
	}

	private static ColorByte calculateColorMix(BodyPart parent, ColorByte color) {
		if (parent == null)
			return color;
		return parent.getColor().mix(color);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, new ColorByte(0), new DelayNerve(13), 0, null);
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	private int getOrientationSign() {
		return (int) Math.signum(angleToParentAtRest);
	}

	@Override
	public boolean equals(Object obj) {
		if (angleToParentAtRest != ((BodySegment) obj).angleToParentAtRest)
			return false;
		return super.equals(obj);
	}
}
