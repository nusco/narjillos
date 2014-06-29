package org.nusco.swimmers.creature.body.pns;

import org.nusco.swimmers.physics.Vector;

public class TrackingNerve implements Nerve {

	static final double ROTATION_SPEED = 0.5;
	static final double ROTATION_HISTERESIS = ROTATION_SPEED;

	private Vector currentDirection = Vector.polar(180, 1);

	void setAngle(double angle) {
		currentDirection = Vector.polar(angle, currentDirection.getLength());
	}

	public Vector tick(Vector inputSignal) {
		double currentAngle = currentDirection.getAngle();
		double newAngle = currentAngle + correctAngle(inputSignal);
		currentDirection = Vector.polar(newAngle, inputSignal.getLength());
		return currentDirection;
	}
	
	private double correctAngle(Vector inputSignal) {
		double difference = inputSignal.getAngleWith(currentDirection);
		if(Math.abs(difference) < ROTATION_HISTERESIS)
			return 0;
		double sign = Math.signum(180 - Math.abs(difference));
		double unsignedResult = ROTATION_SPEED * Math.signum(difference);
		return sign * unsignedResult;
	}
}