package org.nusco.swimmer.brain;

public class Brain {

	private Behaviour behaviour = Behaviour.FEEDING;
	
	public Behaviour getBehaviour() {
		return behaviour;
	}

	public void reachGoal() {
		if(behaviour == Behaviour.FEEDING)
			behaviour = Behaviour.MATING;
		else
			behaviour = Behaviour.FEEDING;
	}
}
