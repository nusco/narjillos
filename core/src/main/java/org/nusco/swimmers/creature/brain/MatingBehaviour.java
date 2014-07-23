package org.nusco.swimmers.creature.brain;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class MatingBehaviour extends Behaviour {
	public MatingBehaviour() {
		super("mating");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.findNarjillo(self);
	}
}
