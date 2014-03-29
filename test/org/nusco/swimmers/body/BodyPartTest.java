package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BodyPartTest extends PartTest {
	private Part parent;
	
	@Override
	public Part createPart() {
		parent = new HeadPart(15, THICKNESS);
		return new BodyPart(20, THICKNESS, parent, 10);
	}

	@Test
	public void hasAParent() {
		assertEquals(parent, ((BodyPart)part).getParent());
	}

	@Test
	public void startsAtItsParentsEndPoint() {
		assertEquals(parent.getEndPoint(), part.getStartPoint());
	}
	
	@Test
	public void hasAnInitialAngleRelativeToTheParent() {
		assertEquals(10, part.getAngle());
	}
	
	@Test
	public void theAngleRelativeToTheParentStaysInThe0To360DegreesRange() {
		part.setRelativeAngle(350);
		assertEquals(350, part.getAngle());
		parent.setRelativeAngle(10);
		assertEquals(0, part.getAngle());
		parent.setRelativeAngle(20);
		assertEquals(10, part.getAngle());
	}

	@Override
	public void hasAnEndPoint() {
		parent.setRelativeAngle(90);
		part.setRelativeAngle(-90);
		assertEquals(new Vector(20, 15), part.getEndPoint());
	}
}
