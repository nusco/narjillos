package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegmentTest extends OrganTest {
	private BodyPart parent;
	
	@Override
	public BodyPart createConcreteBodyPart(int length, int thickness) {
		parent = new Head(10, 5, new ColorByte(100), 1);
		return new BodySegment(20, 10, new ColorByte(100), new DelayNerve(10), 0, parent);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, part.getParent());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), part.getStartPoint());
	}
	
	@Test
	public void hasAnAbsoluteAngle() {
		Head head = new Head(0, 0, new ColorByte(100), 1);
		BodyPart organ1 = new BodySegment(0, 0, new ColorByte(100), new PassNerve(), 30, head);
		Organ organ2 = new BodySegment(0, 0, new ColorByte(100), new PassNerve(), -10, organ1);
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(10, 0, new ColorByte(100), 1);
		BodyPart organ1 = head.sproutOrgan(10, 0, new ColorByte(100), 0, 90);
		Organ organ2 = organ1.sproutOrgan(10, 0, new ColorByte(100), 0, -90);
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}
}
