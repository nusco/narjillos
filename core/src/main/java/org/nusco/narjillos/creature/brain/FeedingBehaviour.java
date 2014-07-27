package org.nusco.narjillos.creature.brain;

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;

public class FeedingBehaviour extends Behaviour {
	public FeedingBehaviour() {
		super("feeding");
	}
	
	public Vector acquireTarget(Pond pond, Vector self) {
		return pond.findFoodPiece(self).getPosition();
	}
}
