package org.nusco.swimmer.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.CosWave;
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

	@Test
	public void emitsASinusoidalWaveWhileTicking() {
		Head head = new Head(0, 0, 0);
		
		for (int i = 0; i < CosWave.WAVE.length; i++) {
			assertEquals(CosWave.WAVE[i], head.getNerve().readOutputSignal(), 0.1);
			for (int j = 0; j < 10; j++)
				head.tick();
		}
	}
}
