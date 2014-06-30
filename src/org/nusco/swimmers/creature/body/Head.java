package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

public class Head extends Organ {

	static final double ROTATION_SPEED = 0.5;
	static final double ROTATION_HISTERESIS = ROTATION_SPEED;

	private Vector startPoint = Vector.ZERO;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, rgb, new PassNerve(), null);
		setAngleToParent(0);
		tick(Vector.ZERO);
	}
	
	@Override
	public double getAbsoluteAngle() {
		return getAngleToParent();
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector point) {
		this.startPoint = point;
	}

	protected Vector getMainAxis() {
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
}
