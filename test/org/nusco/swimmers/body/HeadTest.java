package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HeadTest extends VisibleOrganTest {

	@Override
	public VisibleOrgan createVisibleOrgan() {
		return new Head(20, THICKNESS, 100);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(new Vector(0, 0), organ.getStartPoint());
	}

	@Test
	public void hasAngleRelativeToTheCenterPointOfZeroByDefault() {
		assertEquals(0, organ.getAngle(), 0);
	}
	
	@Test
	public void theAngleToTheCenterPointIsNormalisedInDegrees() {
		organ.setRelativeAngle(25);
		assertEquals(25, organ.getAngle(), 0);
		organ.setRelativeAngle(360);
		assertEquals(0, organ.getAngle(), 0);
		organ.setRelativeAngle(362);
		assertEquals(2, organ.getAngle(), 0);
		organ.setRelativeAngle(-2);
		assertEquals(358, organ.getAngle(), 0);
	}

	@Test
	public void canBeSetAtADifferentPoint() {
		((Head)organ).placeAt(new Vector(20, 30));
		assertEquals(new Vector(20, 30), organ.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(new Vector(20, 0), organ.getEndPoint());
		organ.setRelativeAngle(45);
		assertEquals(new Vector(14, 14), organ.getEndPoint());
		organ.setRelativeAngle(90);
		assertEquals(new Vector(0, 20), organ.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, organ.getParent());
	}
}
