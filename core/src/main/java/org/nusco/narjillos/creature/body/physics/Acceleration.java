package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Vector;

public class Acceleration {

	private final Vector linear;
	public final double angular;
	public final double energySpent;

	public Acceleration(Vector linear, double angular, double energySpent) {
		this.linear = linear;
		this.angular = angular;
		this.energySpent = energySpent;
	}

	public Vector getLinearAccelerationAlong(Vector axis) {
		return linear.getProjectionOn(axis);
	}
}
