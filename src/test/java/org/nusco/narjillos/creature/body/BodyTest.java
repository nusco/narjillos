package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.physics.Vector;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		Body body = new Body(new Head(1, 1, 1, 1, 1, 1, Element.OXYGEN, 0.5, 1, 0));
		assertEquals(Vector.ZERO, body.getStartPoint());
	}

	@Test
	public void canBeForcedAtAGivenPosition() {
		Body body = new Body(new Head(1, 1, 1, 1, 1, 1, Element.OXYGEN, 0.5, 1, 0));
		
		body.forcePosition(Vector.cartesian(10, -10), 15);

		assertEquals(Vector.cartesian(10, -10), body.getStartPoint());
	}
	
	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(headLengthInMm, headThicknessInMm, 0, 0, 0, 1, Element.OXYGEN, 0.5, 1, 0);
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodyPart(segmentLengthInMm, segmentThicknessInMm, 0, 0, 0, head, 0, 0, 0, 0));
		Body body = new Body(head);
		
		double expectedMassInGrams = 212;
		assertEquals(expectedMassInGrams, body.getAdultMass(), 0.001);
	}
	
	@Test
	public void hasACenterOfMassAndARadius() {
		Head head = new Head(10, 10, 0, 0, 0, 1, Element.OXYGEN, 0.5, 1, 0);
		head.addChild(new BodyPart(20, 5, 0, 0, 0, head, 0, 0, 0, 0));
		Body body = new Body(head);

		body.growToAdultForm();
		
		// calculateRadius() needs an explicit center of mass, because of optimizations.
		// So these two are better tested together:
		assertEquals(Vector.cartesian(12.5, 0), body.getCenterOfMass());
		assertEquals(17.5, body.getRadius(), 0.0);
	}
	
	@Test
	public void itsMinimumRadiusIsOne() {
		Head head = new Head(0, 1, 0, 0, 0, 1, Element.OXYGEN, 0.5, 0, 0);
		Body body = new Body(head);
		assertEquals(1, body.getRadius(), 0.0);
	}
	
	@Test
	public void hasTheSameByproductAsTheHead() {
		Head head = new Head(0, 1, 0, 0, 0, 1, Element.OXYGEN, 0.42, 0, 10);
		Body body = new Body(head);

		assertEquals(Element.OXYGEN, head.getByproduct());
		assertEquals(Element.OXYGEN, body.getByproduct());
	}

	@Test
	public void hasTheSameEnergyToChildrenAsTheHead() {
		Body body = new Body(new Head(0, 1, 0, 0, 0, 1, Element.OXYGEN, 0.42, 0, 0));

		assertEquals(0.42, body.getEnergyToChildren(), 0.0);
	}
	
	@Test
	public void hasTheSameEggVelocityAsTheHead() {
		Body body = new Body(new Head(0, 1, 0, 0, 0, 1, Element.OXYGEN, 0.42, 42, 0));

		assertEquals(42, body.getEggVelocity(), 0.0);
	}
	
	@Test
	public void hasTheSameEggIntervalAsTheHead() {
		Body body = new Body(new Head(0, 1, 0, 0, 0, 1, Element.OXYGEN, 0.42, 0, 10));

		assertEquals(10, body.getEggInterval(), 0.0);
	}
}
