package org.nusco.narjillos.creature.body.physics;

public class Viscosity {

	public static final double KICK_IN_VELOCITY = 300;
	public static final double MAX_VELOCITY = 400;
	
	public static double limit(double velocity) {
		if (velocity <= KICK_IN_VELOCITY + 1)
			return velocity;

		double result = KICK_IN_VELOCITY + 1 + Math.log(velocity - KICK_IN_VELOCITY);
		return Math.min(MAX_VELOCITY, result);
	}
}
