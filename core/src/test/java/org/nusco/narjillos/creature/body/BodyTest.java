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
		assertEquals(Vector.ZERO, body.getStartPoint());
	}

	@Test
	public void canBeTeleportedToAGivenPosition() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1));
		
		body.teleportTo(Vector.cartesian(10, -10));

		assertEquals(Vector.cartesian(10, -10), body.getStartPoint());
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
	
	@Test
	public void hasHalfItsMaximumLengthAsARadius() {
		Head head = new Head(1, 1, new ColorByte(1), 1);
		
		BodySegment longFirstLevelChild = new BodySegment(5, 0, new ColorByte(0), head, null, 0, 0);
		head.addChild(longFirstLevelChild);
		
		BodySegment shortFirstLevelChild = new BodySegment(3, 0, new ColorByte(0), head, null, 0, 0);
		head.addChild(shortFirstLevelChild);
		
		BodySegment veryShortSecondLevelChild = new BodySegment(1, 0, new ColorByte(0), longFirstLevelChild, null, 0, 0);
		longFirstLevelChild.addChild(veryShortSecondLevelChild);
		
		BodySegment anotherVeryShortSecondLevelChild = new BodySegment(1, 0, new ColorByte(0), shortFirstLevelChild, null, 0, 0);
		shortFirstLevelChild.addChild(anotherVeryShortSecondLevelChild);
		
		BodySegment veryLongSecondLevelChild = new BodySegment(10, 0, new ColorByte(0), shortFirstLevelChild, null, 0, 0);
		shortFirstLevelChild.addChild(veryLongSecondLevelChild);

		Body body = new Body(head);
		assertEquals(7, body.getMaxRadius(), 0.0);
	}	
	
	@Test
	public void itsMinimumRadiusIsOne() {
		Head head = new Head(0, 1, new ColorByte(1), 1);
		Body body = new Body(head);
		assertEquals(1, body.getMaxRadius(), 0.0);
	}	
}
