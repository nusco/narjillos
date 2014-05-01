package org.nusco.swimmer.brain;

import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.world.World;

public class Behaviour {

	public static final Behaviour FEEDING = new FeedingBehaviour();
	public static final Behaviour MATING = new Behaviour("mating");
	
	private final String name;

	public Behaviour(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Vector lookAt(World world, Vector self) {
		Vector target = world.closestFoodTo(self);
		return target.minus(self).normalize();
	}
}
