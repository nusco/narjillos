package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HeadPartTest extends PartTest {

	@Override
	public Part createPart() {
		return new HeadPart(20, THICKNESS);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(new Vector(0, 0), part.getStartPoint());
	}

	@Test
	public void hasAngleRelativeToTheCenterPointOfZeroByDefault() {
		assertEquals(0, part.getAngle());
	}
	
	@Test
	public void theAngleToTheCenterPointIsNormalisedInDegrees() {
		part.setRelativeAngle(25);
		assertEquals(25, part.getAngle());
		part.setRelativeAngle(360);
		assertEquals(0, part.getAngle());
		part.setRelativeAngle(362);
		assertEquals(2, part.getAngle());
		part.setRelativeAngle(-2);
		assertEquals(358, part.getAngle());
	}

	@Test
	public void canBeSetAtADifferentPoint() {
		((HeadPart)part).placeAt(new Vector(20, 30));
		assertEquals(new Vector(20, 30), part.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(new Vector(20, 0), part.getEndPoint());
		part.setRelativeAngle(45);
		assertEquals(new Vector(14, 14), part.getEndPoint());
		part.setRelativeAngle(90);
		assertEquals(new Vector(0, 20), part.getEndPoint());
	}
}
