package org.nusco.narjillos.creature.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;

public class BehaviourTest {

	Pond pond = new Pond(1000);

	@Before
	public void setupPond() {
		pond.spawnFood(Vector.cartesian(10, 10));
		pond.spawnFood(Vector.cartesian(100, 100));
		pond.spawnNarjillo(Vector.cartesian(50, 30), DNA.random());
		pond.spawnNarjillo(Vector.cartesian(30, 100), DNA.random());
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
