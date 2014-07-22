package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.DelayNerve;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.shared.physics.Vector;

public class BodySegment extends BodyPart {
	private static final double AMPLITUDE_MULTIPLIER = 1.5;
	private static final int MAX_ROTATION_SPEED = 5;
	
	private final double angleToParentAtRest;
	
	public BodySegment(int length, int thickness, int angleToParentAtRest, int rgb, BodyPart parent, int delay) {
		super(length, thickness, rgb, parent, new DelayNerve(delay));
		this.angleToParentAtRest = angleToParentAtRest;
		setAngleToParent(angleToParentAtRest);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, 0, 0, null, 13);
	}

	@Override
	public int calculateColor() {
		final int redPosition = 0b00000111;
		final int greenPosition = 0b00111000;
		final int bluePosition = 0b11000000;

		int red = (getHue() & redPosition);
		int green = ((getHue() & greenPosition) >> 3);
		int blue = ((getHue() & bluePosition) >> 5);

		double parentRed = (getParent().getColor() & redPosition);
		double parentGreen = ((getParent().getColor() & greenPosition) >> 3);
		double parentBlue = ((getParent().getColor() & bluePosition) >> 5);
		
		final double dampening = 0.3;
		int newRed = ((int)(parentRed * dampening) + red) & 0b111;
		int newGreen = ((int)(parentGreen * dampening) + green) & 0b111;
		int newBlue = ((int)(parentBlue * dampening) + blue) & 0b011;
		
		return newRed | (newGreen << 3) | (newBlue << 5);
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
		return (int)Math.signum(angleToParentAtRest);
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
		if(angleToParentAtRest != ((BodySegment)obj).angleToParentAtRest)
			return false;
		return super.equals(obj);
	}
}
