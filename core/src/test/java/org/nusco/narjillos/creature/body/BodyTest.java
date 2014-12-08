package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1, 0.5));
		assertEquals(Vector.ZERO, body.getStartPoint());
	}

	@Test
	public void canBeTeleportedToAGivenPosition() {
		Body body = new Body(new Head(1, 1, new ColorByte(1), 1, 0.5));
		
		body.teleportTo(Vector.cartesian(10, -10));

		assertEquals(Vector.cartesian(10, -10), body.getStartPoint());
	}
	
	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, new ColorByte(0), 1, 0.5);
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodySegment(segmentLengthInMm, segmentThicknessInMm, new ColorByte(0), head, 0, 0, 0, 0));
		Body body = new Body(head);
		
		double expectedMassInGrams = 212;
		assertEquals(expectedMassInGrams, body.getAdultMass(), 0.001);
	}
	
	@Test
	public void hasACenterOfMassAndARadius() {
		Head head = new Head(10, 10, new ColorByte(1), 1, 0.5);
		
		BodySegment child = new BodySegment(20, 5, new ColorByte(0), head, 0, 0, 0, 0);
		head.addChild(child);

		Body body = new Body(head);

		// TODO: why doesn't it work if I just tick the body?
//		for (int i = 0; i < 1000; i++)
//			body.tick(Vector.polar(100, 100));
		grow(head);
		grow(child);
		
		// calculateRadius() needs an explicit center of mass, because of optimizations.
		// So these two are better tested together: 
		Vector centerOfMass = body.calculateCenterOfMass();
		assertEquals(Vector.cartesian(12.5, 0), centerOfMass);
		
		assertEquals(17.5, body.calculateRadius(body.calculateCenterOfMass()), 0.0);
	}

	private void grow(Organ organ) {
		for (int i = 0; i < 1000; i++)
			organ.tick(1, 0);
	}	
	
	@Test
	public void itsMinimumRadiusIsOne() {
		Head head = new Head(0, 1, new ColorByte(1), 1, 0.5);
		Body body = new Body(head);
		assertEquals(1, body.calculateRadius(head.getCenterOfMass()), 0.0);
	}
	
	@Test
	public void hasTheSamePercentEnergyToChildrenAsTheHead() {
		Body body = new Body(new Head(0, 1, new ColorByte(1), 1, 0.42));

		assertEquals(0.42, body.getPercentEnergyToChildren(), 0.0);
	}
}
