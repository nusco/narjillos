package org.nusco.swimmer.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;
import org.nusco.swimmer.things.Food;

public class BrainTest {

	@Test
	public void aimsForADirection() {
		Brain brain = new Brain();
		Pond pond = new Pond();
		pond.add(new Food(), 0, 100);
		Vector self = Vector.ZERO;
		
		Vector direction = brain.getDirection(pond, self);
		
		assertEquals(90, direction.getAngle(), 0.001);
		assertEquals(1, direction.getLength(), 0.001);
	}

	@Test
	public void cyclesThroughMatingAndFeedingBehaviour() {
		Brain brain = new Brain();
		assertEquals("feeding", brain.getBehaviour().toString());
		brain.reachGoal();
		assertEquals("mating", brain.getBehaviour().toString());
		brain.reachGoal();
		assertEquals("feeding", brain.getBehaviour().toString());
	}
}
