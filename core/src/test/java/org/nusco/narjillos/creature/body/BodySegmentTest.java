package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegmentTest extends OrganTest {
	private Organ parent;
	
	@Override
	public Organ createConcreteOrgan(int length, int thickness) {
		parent = new Head(10, 5, new ColorByte(100), 1, 0.5);
		return new BodySegment(length, thickness, new ColorByte(100), parent, 10, 0, 1, 0);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, getBodyPart().getParent());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), getBodyPart().getStartPoint());
	}
	
	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(0, 0, new ColorByte(100), 1, 0.5);
		Organ organ1 = new BodySegment(0, 0, new ColorByte(100), head, 0, 30, 1, 0);
		BodyPart organ2 = new BodySegment(0, 0, new ColorByte(100), organ1, 0, -10, 1, 0);
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}
	
	@Test
	public void hasAnAmplitude() {
		Head head = new Head(0, 0, new ColorByte(100), 1, 0.5);
		BodySegment organ = new BodySegment(0, 0, new ColorByte(100), head, 0, -10, 42, 0);
		assertEquals(42, organ.getAmplitude(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(10, 0, new ColorByte(100), 1, 0.5);
		Organ organ1 = head.addChild(new BodySegment(10, 0, new ColorByte(100), head, 0, 90, 1, 0));
		Organ organ2 = organ1.addChild(new BodySegment(10, 0, new ColorByte(100), organ1, 0, -90, 1, 0));

		fullyGrow(head);
		fullyGrow(organ1);
		fullyGrow(organ2);
		
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 0, new ColorByte(100), 1, 0.5);
		Organ organ = head.addChild(new BodySegment(10, 0, new ColorByte(100), head, 0, 20, 1, 0));
		// uses the current angle, not the angle at rest
		organ.updateAngleToParent(45);
		
		fullyGrow(organ);
		
		final double lengthAt45Degrees = 7.07106;
		double expectedX = head.getEndPoint().x + lengthAt45Degrees / 2;
		double expectedY = head.getEndPoint().y + lengthAt45Degrees / 2;
		Vector expected = Vector.cartesian(expectedX, expectedY);
		assertTrue(organ.getCenterOfMass().almostEquals(expected));
	}
}
