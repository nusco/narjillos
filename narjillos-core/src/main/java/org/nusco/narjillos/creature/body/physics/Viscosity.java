package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.utilities.Configuration;

public class Viscosity {

	public static double limit(double velocity) {
		velocity = Math.min(Viscosity.PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE, velocity);

		if (velocity <= Configuration.PHYSICS_VISCOSITY_KICK_IN_VELOCITY + 1)
			return velocity;
		
		return Configuration.PHYSICS_VISCOSITY_KICK_IN_VELOCITY + 1 + FastMath.log(velocity - Configuration.PHYSICS_VISCOSITY_KICK_IN_VELOCITY);
	}

	public static double getMaxVelocity() {
		return limit(Viscosity.PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE);
	}

	public static final double PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE = Configuration.PHYSICS_VISCOSITY_KICK_IN_VELOCITY + FastMath.LOG_MAX;
}
