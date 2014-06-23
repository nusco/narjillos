package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.creature.pns.WaveNerve;
import org.nusco.swimmers.physics.Vector;

public class Head extends Organ {
	
	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;
	private static final double ROTATION_SPEED = 0.5;

	private Vector startPoint = Vector.ZERO;
	private double angle;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb, new WaveNerve(WAVE_SIGNAL_FREQUENCY), null);
		angle = getRelativeAngle();
	}

	Head(Nerve nerve) {
		super(0, 0, 0, 0, nerve, null);
		angle = getRelativeAngle();
	}

	@Override
	public Organ getParent() {
		return null;
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public Vector tick(Vector inputSignal) {
		Vector result = super.tick(inputSignal);
		angle += correctAngle(inputSignal);
		return result;
	}

	private double correctAngle(Vector inputSignal) {
		Vector inverseVector = getVector().invert();
		double difference = inputSignal.getAngleWith(inverseVector);
		if(Math.abs(difference) < ROTATION_SPEED * 2)
			return 0;
		double unsignedResult = ROTATION_SPEED * Math.signum(difference);
		if(Math.abs(difference) <= 180)
			return unsignedResult;
		else
			return -unsignedResult;
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}
}
