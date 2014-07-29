package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Vector;

public class Acceleration {

	private final Vector linear;
	public double angular;
	public final double energySpent;

	public Acceleration(Vector movement, double rotationAngle, double energySpent) {
		this.linear = movement;
		this.angular = rotationAngle;
		this.energySpent = energySpent;
	}

	public Vector getLinearAccelerationAlong(Vector axis) {
		return linear.getProjectionOn(axis);
	}

}
