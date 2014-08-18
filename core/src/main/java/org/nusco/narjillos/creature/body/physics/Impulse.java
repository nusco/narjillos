package org.nusco.narjillos.creature.body.physics;

import org.nusco.narjillos.shared.physics.Vector;

public class Impulse {

	public final Vector linearComponent;
	public final double angularComponent;
	public final double energySpent;

	public Impulse(Vector linear, double angularComponent, double energySpent) {
		this.linearComponent = linear;
		this.angularComponent = angularComponent;
		this.energySpent = energySpent;
	}
}
