package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.creature.physics.ForceField;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class Head extends BodyPart {

	static final double ROTATION_SPEED = 0.5;
	static final double ROTATION_HISTERESIS = ROTATION_SPEED;

	private final double metabolicRate;
	
	public Head(int length, int thickness, ColorByte hue, double metabolicRate) {
		super(length, thickness, hue, null, new PassNerve());
		this.metabolicRate = metabolicRate;
		setAngleToParent(0);
		tick(Vector.ZERO);
	}

	@Override
	protected Vector calculateStartPoint() {
		return Vector.ZERO;
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getAngleToParent();
	}

	@Override
	protected ColorByte calculateColor() {
		return getHue();
	}

	@Override
	protected Vector calculateMainAxis() {
		return getVector().normalize(1);
	}

	@Override
	protected void move(Vector signal) {
		setAngleToParent(getAngleToParent() + turnTowards(signal.invert()));
	}

	private double turnTowards(Vector inputSignal) {
		double difference = inputSignal.getAngleWith(getVector());
		
		// special case: in case the main axis is exactly opposite to the target
		if (Math.abs(difference - 180) < 2)
			difference = -178;

		if (Math.abs(difference) < ROTATION_HISTERESIS)
			return 0;
		double sign = Math.signum(180 - Math.abs(difference));
		double unsignedResult = ROTATION_SPEED * Math.signum(difference);
		return sign * unsignedResult;
	}

	public Organ sproutNeck() {
		return addChild(new Neck(this, getMetabolicRate()));
	}

	public double getMetabolicRate() {
		return metabolicRate;
	}

	public ForceField createForceField() {
		ForceField result = new ForceField();
		setMovementListener(result);
		return result;
	}
}
