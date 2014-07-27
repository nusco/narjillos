package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends BodySegment {

	private static final double ROTATION_SPEED = 0.5;
	private static final double ROTATION_HISTERESIS = ROTATION_SPEED;

	private final double metabolicRate;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, new PassNerve(), 0, null);
		this.metabolicRate = metabolicRate;
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}

	@Override
	protected Vector calculateStartPoint() {
		return Vector.ZERO;
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}
	
	protected Vector getMainAxis() {
		return getVector().normalize(1).invert();
	}

	@Override
	protected double calculateAngleToParent(double targetAngle, ForceField forceField) {
		return getAngleToParent(); // don't bother with this now (see below)
	}

	public void rotateTowards(Vector direction) {
		// HACK. will stay in place until I have real physical rotation
		double difference = direction.invert().getAngleWith(getVector());
		
		// special case: in case the main axis is exactly opposite to the target
		if (Math.abs(difference - 180) < 2)
			difference = -178;

		if (Math.abs(difference) < ROTATION_HISTERESIS)
			return;

		double sign = Math.signum(180 - Math.abs(difference));
		double unsignedResult = ROTATION_SPEED * Math.signum(difference);
		double targetAngleToParent = getAngleToParent() + sign * unsignedResult;
		
		double rotationSpeed = targetAngleToParent - getAngleToParent();

		setAngleToParent(normalize(getAngleToParent() + rotationSpeed));
	}

	private double normalize(double degrees) {
		// check that this code makes sense. I probably scrap together with rotateTowards() anyways
		degrees = ((degrees % 360) + 360) % 360;
		if (degrees > 180)
			degrees = -(360-degrees);
		return degrees;
	}
}