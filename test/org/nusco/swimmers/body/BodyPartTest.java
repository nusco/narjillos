package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BodyPartTest extends VisibleOrganTest {
	private VisibleOrgan parent;
	
	@Override
	public VisibleOrgan createVisibleOrgan() {
		parent = new Head(15, THICKNESS, 100);
		return new BodyPart(20, THICKNESS, parent, 10, 100);
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), organ.getStartPoint());
	}
	
	@Test
	public void hasAnInitialAngleRelativeToTheParent() {
		assertEquals(10, organ.getAngle(), 0);
	}
	
	@Test
	public void theAngleRelativeToTheParentStaysInThe0To360DegreesRange() {
		organ.setRelativeAngle(350);
		assertEquals(350, organ.getAngle(), 0);
		parent.setRelativeAngle(10);
		assertEquals(0, organ.getAngle(), 0);
		parent.setRelativeAngle(20);
		assertEquals(10, organ.getAngle(), 0);
	}

	@Override
	public void hasAnEndPoint() {
		parent.setRelativeAngle(90);
		organ.setRelativeAngle(-90);
		assertEquals(new Vector(20, 15), organ.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(parent, organ.getParent());
	}
}
