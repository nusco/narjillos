package org.nusco.swimmers.creature;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.genetics.DNA;

public class NarjilloTest {

	@Test
	public void hasAMassProportionalToItsSize() {
		
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, 0, 1);
		
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.sproutOrgan(segmentLengthInMm, segmentThicknessInMm, 0, 0, 0);
		Narjillo narjillo = new Narjillo(head, DNA.random());
		
		double expectedMassInGrams = 21.2;
		assertEquals(expectedMassInGrams, narjillo.getMass(), 0.001);
	}
}
