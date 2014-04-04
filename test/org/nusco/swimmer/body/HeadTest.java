package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

public class HeadTest extends VisibleOrganTest {

	@Override
	public Head createVisibleOrgan() {
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
	public void hasAngleRelativeToTheParentZero() {
		assertEquals(0, organ.getRelativeAngle(), 0);
	}

	@Test
	public void canBeSetAtADifferentPoint() {
		((Head)organ).placeAt(new Vector(20, 30));
		assertEquals(new Vector(20, 30), organ.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(new Vector(20, 0), organ.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, organ.getParent());
	}

	@Override
	public void hasAVectorRelativeToItsParent() {
		assertEquals(new Vector(20, 0), organ.getRelativeVector());
	}

	@Test
	public void emitsASinusoidalSignalWhileTicking() {
		Head head = new Head(0, 0, 0);

		double[] expectedSignal = new double[] { 1.0000, 0.9980, 0.9921, 0.9823, 0.9686, 0.9511, 0.9298 };

		for (int i = 0; i < expectedSignal.length; i++) {
			Vector expected = new Vector(expectedSignal[i], expectedSignal[i]);
			head.tick();
			Vector outputSignal = head.getNerve().readOutputSignal();
			assertEquals(expected.getX(), outputSignal.getX(), 0.0001);
			assertEquals(expected.getY(), outputSignal.getY(), 0.0001);
		}
	}
}
