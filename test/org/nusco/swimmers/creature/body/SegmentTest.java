package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.physics.Vector;

public class SegmentTest extends OrganTest {
	private Organ parent;
	
	@Override
	public Organ createOrgan() {
		parent = new Head(15, THICKNESS, 100);
		return new Segment(20, THICKNESS, 10, 100, parent);
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, organ.getParent());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), organ.getStartPoint());
	}
	
	@Test
	public void hasAnAngleAtRest() {
		Head head = new Head(0, 0, 0);
		Organ organ1 = new Segment(0, 0, 30, 0, head);
		Organ organ2 = new Segment(0, 0, -10, 0, organ1);
		assertEquals(20, organ2.getAbsoluteAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		Head head = new Head(10, 0, 0);
		Organ organ1 = head.sproutOrgan(10, 0, 90, 0);
		Organ organ2 = organ1.sproutOrgan(10, 0, -90, 0);
		assertEquals(Vector.cartesian(20, 10), organ2.getEndPoint());
	}
}
