package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodyPartTest extends ConnectedOrganTest {
	
	private ConnectedOrgan parent;
	
	@Override
	public ConnectedOrgan createConcreteOrgan(int length, int thickness) {
		parent = new Head(10, 5, new ColorByte(100), 1, 0.5);
		return new BodyPart(length, thickness, new ColorByte(100), parent, 10, 0, 1, 0);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, getOrgan().getParent());
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(10, 0, new ColorByte(100), 1, 0.5);
		ConnectedOrgan organ1 = head.addChild(new BodyPart(10, 0, new ColorByte(100), head, 0, 90, 1, 0));
		ConnectedOrgan organ2 = organ1.addChild(new BodyPart(10, 0, new ColorByte(100), organ1, 0, -90, 1, 0));

		fullyGrow(head);
		fullyGrow(organ1);
		fullyGrow(organ2);
		
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), getOrgan().getStartPoint());
	}
	
	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(0, 0, new ColorByte(100), 1, 0.5);
		ConnectedOrgan organ1 = new BodyPart(0, 0, new ColorByte(100), head, 0, 30, 1, 0);
		Organ organ2 = new BodyPart(0, 0, new ColorByte(100), organ1, 0, -10, 1, 0);
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}
	
	@Test
	public void hasAnAmplitude() {
		Head head = new Head(0, 0, new ColorByte(100), 1, 0.5);
		BodyPart organ = new BodyPart(0, 0, new ColorByte(100), head, 0, -10, 42, 0);
		assertEquals(42, organ.getAmplitude(), 0);
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 0, new ColorByte(100), 1, 0.5);
		MovingOrgan organ = (MovingOrgan) head.addChild(new BodyPart(10, 0, new ColorByte(100), head, 0, 20, 1, 0));
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
