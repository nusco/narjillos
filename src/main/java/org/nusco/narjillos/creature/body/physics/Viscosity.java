package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.core.geometry.FastMath;
import org.nusco.narjillos.core.utilities.Configuration;

/**
 * Quick hacky functions to calculate limited velocity in a viscous fluid. No
 * relation to real-life physics, but good enough for our needs. We just want
 * to give dimishing returns to narjillos that get overly fast.
 */
public class Viscosity {

	private static final double PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE = Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY
			+ FastMath.LOG_MAX;

	public static double limit(double velocity) {
		velocity = Math.min(Viscosity.PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE, velocity);

		if (velocity <= Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY + 1)
			return velocity;

		return Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY + 1
				+ FastMath.log(velocity - Configuration.PHYSICS_VISCOSITY_KICKIN_VELOCITY);
	}

	public static double getMaxVelocity() {
		return limit(Viscosity.PHYSICS_VISCOSITY_VELOCITY_CLIPPING_VALUE);
	}
}
