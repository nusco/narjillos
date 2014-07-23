package org.nusco.swimmers.creature.brain;

import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class Brain {

	private Behaviour behaviour = new FeedingBehaviour();

	public void reachGoal() {
		if(behaviour.toString().equals("feeding"))
			behaviour = new MatingBehaviour();
		else
			behaviour = new FeedingBehaviour();
	}
	
	Behaviour getBehaviour() {
		return behaviour;
	}

	public Vector getDirection(Pond pond, Vector self) {
		return behaviour.acquireTarget(pond, self).minus(self).normalize(1);
	}
}
