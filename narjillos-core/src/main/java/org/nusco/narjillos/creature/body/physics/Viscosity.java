package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.FastMath;

public class Viscosity {

	public static final double KICK_IN_VELOCITY = 300;
	public static final double VELOCITY_CLIPPING_VALUE = KICK_IN_VELOCITY + FastMath.LOG_MAX;
	
	public static double limit(double velocity) {
		velocity = Math.min(VELOCITY_CLIPPING_VALUE, velocity);

		if (velocity <= KICK_IN_VELOCITY + 1)
			return velocity;
		
		return KICK_IN_VELOCITY + 1 + FastMath.log(velocity - KICK_IN_VELOCITY);
	}

	public static double getMaxVelocity() {
		return limit(VELOCITY_CLIPPING_VALUE);
	}
}
