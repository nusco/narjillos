package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1));
		assertEquals(Vector.ZERO, body.getPosition());
	}

	@Test
	public void canBePositioned() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1));
		
		body.setPosition(Vector.cartesian(10, -10), 0);

		assertEquals(Vector.cartesian(10, -10), body.getPosition());
	}
	
	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, new ColorByte(0), 1);
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodySegment(segmentLengthInMm, segmentThicknessInMm, new ColorByte(0), head, new DelayNerve(0), 0, 0));
		Body body = new Body(head);
		
		double expectedMassInGrams = 212;
		assertEquals(expectedMassInGrams, body.getMass(), 0.001);
	}
}
