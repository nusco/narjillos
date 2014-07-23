package org.nusco.swimmers.creature.brain;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class FeedingBehaviour extends Behaviour {
	public FeedingBehaviour() {
		super("feeding");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.findFoodPiece(self);
	}
}
