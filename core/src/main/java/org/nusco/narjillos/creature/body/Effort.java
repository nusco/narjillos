package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Vector;

public class Effort {

	public final Vector movement;
	public final double energySpent;

	public Effort(Vector movement, double energySpent) {
		this.movement = movement;
		this.energySpent = energySpent;
	}

}
