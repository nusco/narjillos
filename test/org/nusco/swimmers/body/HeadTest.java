package org.nusco.swimmers.body;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.swimmers.neural.CosWave;
import org.nusco.swimmers.neural.WaveNeuron;

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
	public void hasAWaveNeuron() {
		assertTrue(organ.getNeuron() instanceof WaveNeuron);
	}

	@Test
	public void anglesFollowASinWave() {
		Head head = (Head)organ;
		
		for (int i = 0; i < CosWave.WAVE.length; i++) {
			head.tick();
			assertEquals(CosWave.WAVE[i], head.getAngle(), CosWave.PRECISION);
			assertEquals(CosWave.WAVE[i], head.getRelativeAngle(), CosWave.PRECISION);
		}
	}
}
