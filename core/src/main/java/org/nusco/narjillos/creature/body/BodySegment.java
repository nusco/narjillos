package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

class BodySegment extends BodyPart {
	private static final double AMPLITUDE_MULTIPLIER = 1.5;
	private static final int MAX_ROTATION_SPEED = 5;

	private final double angleToParentAtRest;

	public BodySegment(int length, int thickness, ColorByte hue, Nerve nerve, int angleToParentAtRest, BodyPart parent) {
		super(length, thickness, calculateColorMix(parent, hue), parent, nerve);
		this.angleToParentAtRest = angleToParentAtRest;
		setAngleToParent(angleToParentAtRest);
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

	protected double calculateUpdatedAngle(Vector signal) {
		Vector signedScaledSignal = signal.by(AMPLITUDE_MULTIPLIER * getOrientationSign());

		Vector mainAxis = getMainAxis();
		Vector direction = mainAxis.plus(signedScaledSignal);
		Vector rotatedDirection = direction.rotateBy(angleToParentAtRest - mainAxis.getAngle());

		return incrementAngleToParentBy(rotatedDirection.getAngle());
	}

	private int getOrientationSign() {
		return (int) Math.signum(angleToParentAtRest);
	}

	private double incrementAngleToParentBy(double targetAngleToParent) {
		double targetRotationSpeed = targetAngleToParent - getAngleToParent();
		double rotationSpeed = clip(targetRotationSpeed, MAX_ROTATION_SPEED);
		return getAngleToParent() + rotationSpeed;
	}

	private double clip(double value, double max) {
		if (value > max)
			return max;
		if (value < -max)
			return -max;
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (angleToParentAtRest != ((BodySegment) obj).angleToParentAtRest)
			return false;
		return super.equals(obj);
	}
}
