package org.nusco.swimmer.brain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrainTest {

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
