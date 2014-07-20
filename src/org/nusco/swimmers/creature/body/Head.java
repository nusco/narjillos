package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.creature.physics.ForceField;
import org.nusco.swimmers.shared.physics.Vector;

public class Head extends BodyPart {

	static final double ROTATION_SPEED = 0.5;
	static final double ROTATION_HISTERESIS = ROTATION_SPEED;

	public Head(int length, int thickness, int color) {
		super(length, thickness, color, null, new PassNerve());
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
	protected int calculateColor() {
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
		return addChild(new Neck(this));
	}

	public ForceField createForceField() {
		ForceField result = new ForceField();
		setMovementListener(result);
		return result;
	}
}
