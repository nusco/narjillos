package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegmentTest extends BodyPartTest {
	private Organ parent;
	
	@Override
	public Organ createConcreteOrgan(int length, int thickness) {
		parent = new Head(10, 5, new ColorByte(100), 1);
		return new BodySegment(20, 10, new ColorByte(100), new DelayNerve(10), 0, parent);
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
		Head head = new Head(0, 0, new ColorByte(100), 1);
		Organ organ1 = new BodySegment(0, 0, new ColorByte(100), new PassNerve(), 30, head);
		BodyPart organ2 = new BodySegment(0, 0, new ColorByte(100), new PassNerve(), -10, organ1);
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(10, 0, new ColorByte(100), 1);
		Organ organ1 = head.sproutOrgan(10, 0, new ColorByte(100), 0, 90);
		BodyPart organ2 = organ1.sproutOrgan(10, 0, new ColorByte(100), 0, -90);
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}
	
	@Test
	public void hasACenterOfMass() {
		Head head = new Head(10, 0, new ColorByte(100), 1);
		Organ organ = head.sproutOrgan(10, 0, new ColorByte(100), 0, 20);
		// uses the current angle, not the angle at rest
		organ.setAngleToParent(45);
		
		Vector headCenter = Vector.cartesian(5,  0);
		final double lengthAt45Degrees = 7.07106;
		double expectedX = headCenter.x + lengthAt45Degrees / 2;
		double expectedY = headCenter.y + lengthAt45Degrees / 2;
		Vector expected = Vector.cartesian(expectedX, expectedY);
		assertTrue(organ.getCenterOfMass().almostEquals(expected));
	}
}
