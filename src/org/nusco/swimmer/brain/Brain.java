package org.nusco.swimmer.brain;

import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;

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
		return behaviour.acquireTarget(pond, self).minus(self).normalize();
	}
}
