package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Vector;

public class Effort {

	public final Vector movement;
	public double rotationMovement;
	public final double energySpent;

	public Effort(Vector movement, double rotationAngle, double energySpent) {
		this.movement = movement;
		this.rotationMovement = rotationAngle;
		this.energySpent = energySpent;
	}

}
