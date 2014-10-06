package org.nusco.narjillos.embryogenesis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.DNA;

public class BodyPlanTest {

	@Test
	public void executesChromosomesInASequence() {
		DNA dna = new DNA("{0_100_0_0_0_0_0_0_0}" + "{0_101_0_0_0_0_0_0_0}" + "{0_102_0_0_0_0_0_0_0}");
		Body body = new Embryo(dna).develop();
		
		Organ organ1 = body.getHead();
		assertEquals(100, organ1.getLength());
		assertEquals(1, organ1.getChildren().size());
		
		Organ organ2 = organ1.getChildren().get(0);
		assertEquals(101, organ2.getLength());
		assertEquals(1, organ2.getChildren().size());
		
		Organ organ3 = organ2.getChildren().get(0);
		assertEquals(102, organ3.getLength());
		assertTrue(organ3.getChildren().isEmpty());
	}

	@Test
	public void repeatsSegments() {
		DNA dna = new DNA("{0_100_0_0_0_0_0_0_0}" + "{0_101_0_0_0_0_0_0_0}" + "{0_102_0_0_0_0_0_0_0}");
		// TODO
	}
}
