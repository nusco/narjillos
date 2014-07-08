package org.nusco.swimmers.creature.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.brain.Behaviour;
import org.nusco.swimmers.creature.brain.FeedingBehaviour;
import org.nusco.swimmers.creature.brain.MatingBehaviour;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class BehaviourTest {

	Pond pond = new Pond(1000);

	@Before
	public void setupPond() {
		pond.add(new Food(), Vector.cartesian(10, 10));
		pond.add(new Food(), Vector.cartesian(100, 100));
		pond.add(new Swimmer(new Head(0, 0, 0), DNA.random()), Vector.cartesian(50, 30));
		pond.add(new Swimmer(new Head(0, 0, 0), DNA.random()), Vector.cartesian(30, 100));
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
