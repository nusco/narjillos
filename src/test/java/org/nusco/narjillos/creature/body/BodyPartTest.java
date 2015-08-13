package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;

public class BodyPartTest extends ConnectedOrganTest {
	
	private MovingOrgan parent;
	
	@Override
	public ConnectedOrgan createConcreteOrgan(int length, int thickness) {
		parent = new Head(new HeadParameters());
		return new BodyPart(length, thickness, 100, 101, 102, parent, 10, 0, 1, 0);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, getOrgan().getParent());
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(new HeadParameters(10, 0));
		ConnectedOrgan organ1 = head.addChild(new BodyPart(10, 0, 100, 101, 102, head, 0, 90, 1, 0));
		ConnectedOrgan organ2 = organ1.addChild(new BodyPart(10, 0, 100, 101, 102, organ1, 0, -90, 1, 0));

		head.growToAdultFormWithChildren();
		organ1.growToAdultFormWithChildren();
		organ2.growToAdultFormWithChildren();
		
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		parent.updateGeometry();
		getOrgan().updateGeometry();
		
		assertEquals(parent.getEndPoint(), getOrgan().getStartPoint());
	}
	
	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(new HeadParameters());
		ConnectedOrgan organ1 = new BodyPart(0, 0, 100, 101, 102, head, 0, 30, 1, 0);
		Organ organ2 = new BodyPart(0, 0, 100, 101, 102, organ1, 0, -10, 1, 0);
		
		head.updateGeometry();
		organ1.updateGeometry();
		organ2.updateGeometry();
		
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}
	
	@Test
	public void hasAnAmplitude() {
		Head head = new Head(new HeadParameters());
		BodyPart organ = new BodyPart(0, 0, 100, 101, 102, head, 0, -10, 42, 0);
		
		assertEquals(42, organ.getAmplitude(), 0);
	}
	
	@Test
	public void hasAFiberShiftedFromItsParent() {
		HeadParameters parameters = new HeadParameters();
		parameters.setRed(100);
		parameters.setGreen(101);
		parameters.setBlue(102);
		Head head = new Head(parameters);
		
		BodyPart organ = new BodyPart(0, 0, 10, 20, 30, head, 0, -10, 42, 0);

		assertEquals(new Fiber(110, 121, 132), organ.getFiber());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(new HeadParameters(10, 0));
		MovingOrgan organ = (MovingOrgan) head.addChild(new BodyPart(10, 0, 100, 101, 102, head, 0, 20, 1, 0));
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
