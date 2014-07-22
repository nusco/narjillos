package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.DelayNerve;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.ColorByte;

public class BodySegment extends BodyPart {
	private static final double AMPLITUDE_MULTIPLIER = 1.5;
	private static final int MAX_ROTATION_SPEED = 5;

	private final double angleToParentAtRest;

	public BodySegment(int length, int thickness, int angleToParentAtRest, ColorByte hue, BodyPart parent, int delay) {
		super(length, thickness, hue, parent, new DelayNerve(delay));
		this.angleToParentAtRest = angleToParentAtRest;
		setAngleToParent(angleToParentAtRest);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, 0, new ColorByte(0), null, 13);
	}

	@Override
	public ColorByte calculateColor() {
		return getParent().getColor().mix(getHue());
	}

	private double getAngleToParentAtRest() {
		return angleToParentAtRest;
	}

	@Override
	public double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	@Override
	protected void move(Vector signal) {
		updateAngle(signal);
	}

	private void updateAngle(Vector signal) {
		Vector signedScaledSignal = signal.by(AMPLITUDE_MULTIPLIER * getOrientationSign());

		Vector mainAxis = getMainAxis();
		Vector direction = mainAxis.minus(signedScaledSignal);
		Vector rotatedDirection = direction.rotateBy(getAngleToParentAtRest() - mainAxis.getAngle());

		setAngleToParent(incrementAngleToParentBy(-rotatedDirection.getAngle()));
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
