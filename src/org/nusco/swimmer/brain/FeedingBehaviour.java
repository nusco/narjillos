package org.nusco.swimmer.brain;

import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;

public class FeedingBehaviour extends Behaviour {
	public FeedingBehaviour() {
		super("feeding");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.closestFoodTo(self);
	}
}