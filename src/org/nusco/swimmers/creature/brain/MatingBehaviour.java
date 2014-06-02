package org.nusco.swimmers.creature.brain;

import org.nusco.swimmers.physics.Vector;
import org.nusco.swimmers.pond.Pond;

public class MatingBehaviour extends Behaviour {
	public MatingBehaviour() {
		super("mating");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.closestSwimmerTo(self);
	}
}
