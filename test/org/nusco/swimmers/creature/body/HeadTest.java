package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.WaveNerve;
import org.nusco.swimmers.physics.Vector;

public class HeadTest extends OrganTest {

	@Override
	public Head createOrgan() {
		return new Head(20, THICKNESS, 100);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, organ.getStartPoint());
	}

	@Test
	public void canBeSetAtADifferentPoint() {
		((Head)organ).placeAt(Vector.cartesian(20, 30));
		assertEquals(Vector.cartesian(20, 30), organ.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(Vector.cartesian(20, 0), organ.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, organ.getParent());
	}

	@Test
	public void hasAWaveNerve() {
		Nerve nerve = new Head(0, 0, 0).getNerve();
				
		assertEquals(WaveNerve.class, nerve.getClass());
	}
}
