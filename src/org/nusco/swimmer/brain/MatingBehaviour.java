package org.nusco.swimmer.brain;

import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;

public class MatingBehaviour extends Behaviour {
	public MatingBehaviour() {
		super("mating");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.closestSwimmerTo(self);
	}
}
