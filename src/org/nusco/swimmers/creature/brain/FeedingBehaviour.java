package org.nusco.swimmers.creature.brain;

import org.nusco.swimmers.physics.Vector;
import org.nusco.swimmers.pond.Pond;

public class FeedingBehaviour extends Behaviour {
	public FeedingBehaviour() {
		super("feeding");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.closestFoodTo(self);
	}
}
