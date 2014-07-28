package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Vector;

public class Acceleration {

	private final Vector linearAcceleration;
	public double angularAcceleration;
	public final double energySpent;

	public Acceleration(Vector movement, double rotationAngle, double energySpent) {
		this.linearAcceleration = movement;
		this.angularAcceleration = rotationAngle;
		this.energySpent = energySpent;
	}

	public Vector getAccelerationAlong(Vector axis) {
		// The lateral movement is ignored. Creatures who
		// do too much of it are wasting their energy.
		return linearAcceleration.getProjectionOn(axis);
	}

}
