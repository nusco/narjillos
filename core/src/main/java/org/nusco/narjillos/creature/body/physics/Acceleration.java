package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Vector;

public class Acceleration {

	public final Vector linear;
	public final double angular;
	public final double energySpent;

	public Acceleration(Vector linear, double angular, double energySpent) {
		this.linear = linear;
		this.angular = angular;
		this.energySpent = energySpent;
	}
}
