package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodyTest {

	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, new ColorByte(0), 1);
		
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.sproutOrgan(segmentLengthInMm, segmentThicknessInMm, new ColorByte(0), 0, 0);
		Body body = new Body(head);
		
		double expectedMassInGrams = 21.2;
		assertEquals(expectedMassInGrams, body.getMass(), 0.001);
	}
}
