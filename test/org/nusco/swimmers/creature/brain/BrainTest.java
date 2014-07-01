package org.nusco.swimmers.creature.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.brain.Brain;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.physics.Vector;

public class BrainTest {

	@Test
	public void aimsForADirection() {
		Brain brain = new Brain();
		Pond pond = new Pond();
		pond.add(new Food(), Vector.cartesian(0, 100));
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
