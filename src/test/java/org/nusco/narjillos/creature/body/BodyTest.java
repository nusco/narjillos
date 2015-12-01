package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

public class BodyTest {

	@Test
	public void isPositionedAtZeroByDefault() {
		Body body = new Body(new Head(new HeadParameters()));
		assertEquals(Vector.ZERO, body.getStartPoint());
	}

	@Test
	public void canBeForcedAtAGivenPosition() {
		Body body = new Body(new Head(new HeadParameters()));
		
		body.forcePosition(Vector.cartesian(10, -10), 15);

		assertEquals(Vector.cartesian(10, -10), body.getStartPoint());
	}
	
	@Test
	public void hasAMassProportionalToItsSize() {
		int headLengthInMm = 3;
		int headThicknessInMm = 4;
		Head head = new Head(new HeadParameters(headLengthInMm, headThicknessInMm));
		
		int segmentLengthInMm = 10;
		int segmentThicknessInMm = 20;
		head.addChild(new BodyPart(segmentLengthInMm, segmentThicknessInMm, 0, 0, 0, head, 0, 0, 0, 0));
		Body body = new Body(head);
		
		double expectedMassInGrams = 212;
		assertEquals(expectedMassInGrams, body.getAdultMass(), 0.001);
	}

	@Test
	public void hasACenterOfMassAndARadius() {
		Head head = new Head(new HeadParameters(10, 10));
		head.addChild(new BodyPart(20, 5, 0, 0, 0, head, 0, 0, 0, 0));
		Body body = new Body(head);

		body.growToAdultForm();
		
		// calculateRadius() needs an explicit center of mass, because of optimizations.
		// So these two are better tested together:
		assertEquals(Vector.cartesian(12.5, 0), body.getCenterOfMass());
		assertEquals(17.5, body.getRadius(), 0.0);
	}

	@Test
	public void hasABoundingBox() {
		Head head = new Head(new HeadParameters(10, 1));
		head.addChild(new BodyPart(20, 1, 0, 0, 0, head, 0, 90, 0, 0));
		Body body = new Body(head);

		body.growToAdultForm();
		body.forcePosition(Vector.cartesian(3, 4), 0);

		BoundingBox boundingBox = body.getBoundingBox();
		assertEquals(3, boundingBox.left, 0.0);
		assertEquals(13, boundingBox.right, 0.0);
		assertEquals(4, boundingBox.bottom, 0.0);
		assertEquals(24, boundingBox.top, 0.0);
	}
	
	@Test
	public void itsMinimumRadiusIsOne() {
		Head head = new Head(new HeadParameters(0, 1));
		Body body = new Body(head);
		assertEquals(1, body.getRadius(), 0.0);
	}
	
	@Test
	public void hasTheSameByproductAsTheHead() {
		HeadParameters headParameters = new HeadParameters();
		headParameters.setByproduct(Element.OXYGEN);
		Head head = new Head(headParameters);
		Body body = new Body(head);

		assertEquals(Element.OXYGEN, head.getByproduct());
		assertEquals(Element.OXYGEN, body.getByproduct());
	}

	@Test
	public void hasTheSameEnergyToChildrenAsTheHead() {
		HeadParameters headParameters = new HeadParameters();
		headParameters.setEnergyToChildren(0.42);
		Head head = new Head(headParameters);
		Body body = new Body(head);

		assertEquals(0.42, body.getEnergyToChildren(), 0.0);
	}
	
	@Test
	public void hasTheSameEggVelocityAsTheHead() {
		HeadParameters headParameters = new HeadParameters();
		headParameters.setEggVelocity(42);
		Head head = new Head(headParameters);
		Body body = new Body(head);

		assertEquals(42, body.getEggVelocity(), 0.0);
	}
	
	@Test
	public void hasTheSameEggIntervalAsTheHead() {
		HeadParameters headParameters = new HeadParameters();
		headParameters.setEggInterval(10);
		Head head = new Head(headParameters);
		Body body = new Body(head);

		assertEquals(10, body.getEggInterval(), 0.0);
	}
}
