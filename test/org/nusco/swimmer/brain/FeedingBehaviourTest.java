package org.nusco.swimmer.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.things.Food;
import org.nusco.swimmer.world.World;

public class FeedingBehaviourTest {

	@Test
	public void itAimsForTheClosestPieceOfFood() {
		Behaviour behaviour = new FeedingBehaviour();

		World world = new World();
		world.placeAt(new Food(), 10, 10);

		Vector self = Vector.cartesian(100, 100);
		
		Vector direction = behaviour.lookAt(world, self);
		assertEquals(Vector.polar(-135, 1), direction);
	}
}
