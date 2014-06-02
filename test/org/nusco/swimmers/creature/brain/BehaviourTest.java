package org.nusco.swimmers.creature.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.brain.Behaviour;
import org.nusco.swimmers.creature.brain.FeedingBehaviour;
import org.nusco.swimmers.creature.brain.MatingBehaviour;
import org.nusco.swimmers.physics.Vector;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.pond.Pond;

public class BehaviourTest {

	Pond pond = new Pond();

	@Before
	public void setupPond() {
		pond.add(new Food(), 10, 10);
		pond.add(new Food(), 100, 100);
		pond.add(new Swimmer(null), 50, 30);
		pond.add(new Swimmer(null), 30, 100);
	}
	
	@Test
	public void feedingBehavioursAimForTheClosestPieceOfFood() {
		Behaviour behaviour = new FeedingBehaviour();
		Vector self = Vector.cartesian(30, 30);
		
		Vector direction = behaviour.lookAt(pond, self);

		assertEquals(Vector.polar(-135, 1), direction);
	}
	
	@Test
	public void matingBehavioursAimForTheClosestSwimmer() {
		Behaviour behaviour = new MatingBehaviour();
		Vector self = Vector.cartesian(30, 30);
		
		Vector direction = behaviour.lookAt(pond, self);

		assertEquals(Vector.polar(0, 1), direction);
	}
}
