package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.DelayNerve;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public class BodySegment extends Organ {
	private static final int DELAY = 13;
	private static final double AMPLITUDE_MULTIPLIER = 1.5;
	private static final int MAX_ROTATION_SPEED = 5;
	
	private final double angleToParentAtRest;
	
	public BodySegment(int length, int thickness, int angleToParentAtRest, int rgb, Organ parent) {
		super(length, thickness, rgb, new DelayNerve(DELAY), parent);
		this.angleToParentAtRest = angleToParentAtRest;
		setAngleToParent(angleToParentAtRest);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, 0, 0, null);
	}

	@Override
	public int getColor() {
	    return getParent().getColor() & super.getColor();
	}

	private double getAngleToParentAtRest() {
		return angleToParentAtRest;
	}
	
	@Override
	public double getAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	private int getOrientationSign() {
		return (int)Math.signum(angleToParentAtRest);
	}

	@Override
	protected void move(Vector signal) {
		updateAngle(signal);
	}

	private void updateAngle(Vector signal) {
		Vector mainAxis = getMainAxis();
		
		Vector signedScaledSignal = signal.by(AMPLITUDE_MULTIPLIER * getOrientationSign());
		
		Vector direction = mainAxis.minus(signedScaledSignal);
		Vector rotatedDirection = direction.rotateBy(getAngleToParentAtRest() - mainAxis.getAngle());
		
		setAngleToParent(incrementAngleToParentBy(-rotatedDirection.getAngle()));
	}

	private double incrementAngleToParentBy(double targetAngleToParent) {
		double targetRotationSpeed = targetAngleToParent - getAngleToParent();
		double rotationSpeed = clip(targetRotationSpeed, MAX_ROTATION_SPEED);
		return getAngleToParent() + rotationSpeed;
	}

	private Vector getMainAxis() {
		return getHead().getVector().normalize(1);
	}

	private double clip(double value, double max) {
		if (value > max)
			return max;
		if (value < -max)
			return -max;
		return value;
	}

	private Organ getHead() {
		Organ result = this;
		while(result.getParent() != null)
			result = result.getParent();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(angleToParentAtRest != ((BodySegment)obj).angleToParentAtRest)
			return false;
		return super.equals(obj);
	}
}
