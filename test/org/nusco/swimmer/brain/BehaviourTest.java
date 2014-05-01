package org.nusco.swimmer.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;
import org.nusco.swimmer.things.Food;

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
