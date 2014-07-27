package org.nusco.narjillos.creature.brain;

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;

public class MatingBehaviour extends Behaviour {
	public MatingBehaviour() {
		super("mating");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.findNarjillo(self).getPosition();
	}
}
