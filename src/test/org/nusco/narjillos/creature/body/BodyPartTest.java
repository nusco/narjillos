package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;

public class BodyPartTest extends ConnectedOrganTest {

	private MovingOrgan parent;

	@Override
	public ConnectedOrgan createConcreteOrgan(int length, int thickness) {
		parent = new Head(new HeadParameters());
		return new BodyPart(new BodyPartParameters(length, thickness, parent, 0));
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, getOrgan().getParent());
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(new HeadParameters(10, 0));
		ConnectedOrgan organ1 = head.addChild(new BodyPart(new BodyPartParameters(10, 0, head, 90)));
		ConnectedOrgan organ2 = organ1.addChild(new BodyPart(new BodyPartParameters(10, 0, organ1, -90)));

		head.growToAdultFormWithChildren();
		organ1.growToAdultFormWithChildren();
		organ2.growToAdultFormWithChildren();

		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		parent.update();
		getOrgan().update();

		assertEquals(parent.getEndPoint(), getOrgan().getStartPoint());
	}

	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(new HeadParameters());
		ConnectedOrgan organ1 = new BodyPart(new BodyPartParameters(0, 0, head, 30));
		Organ organ2 = new BodyPart(new BodyPartParameters(0, 0, organ1, -10));

		head.update();
		organ1.update();
		organ2.update();

		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}

	@Test
	public void hasAnAmplitude() {
		Head head = new Head(new HeadParameters());
		BodyPartParameters bodyPartParameters = new BodyPartParameters(0, 0, head, -10);
		bodyPartParameters.setAmplitude(42);
		BodyPart organ = new BodyPart(bodyPartParameters);

		assertEquals(42, organ.getAmplitude(), 0);
	}

	@Test
	public void hasAFiberShiftedFromItsParent() {
		HeadParameters parameters = new HeadParameters();
		parameters.setRed(100);
		parameters.setGreen(101);
		parameters.setBlue(102);
		Head head = new Head(parameters);

		BodyPartParameters bodyPartParameters = new BodyPartParameters(0, 0, head, -10);
		bodyPartParameters.setRedShift(10);
		bodyPartParameters.setGreenShift(20);
		bodyPartParameters.setBlueShift(30);
		BodyPart organ = new BodyPart(bodyPartParameters);

		assertEquals(new Fiber(110, 121, 132), organ.getFiber());
	}

	@Test
	public void hasACenterOfMass() {
		Head head = new Head(new HeadParameters(10, 0));
		MovingOrgan organ = (MovingOrgan) head.addChild(new BodyPart(new BodyPartParameters(10, 0, head, 20)));
		// uses the current angle, not the angle at rest
		organ.setAngleToParent(45);
		head.updateTree();

		organ.growToAdultFormWithChildren();

		final double lengthAt45Degrees = 7.07106;
		double expectedX = head.getEndPoint().x + lengthAt45Degrees / 2;
		double expectedY = head.getEndPoint().y + lengthAt45Degrees / 2;
		Vector expected = Vector.cartesian(expectedX, expectedY);
		assertTrue(organ.getCenterOfMass().approximatelyEquals(expected));
	}
}
