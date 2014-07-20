package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.shared.physics.Vector;

public class HeadTest extends OrganTest {

	@Override
	public Head createOrgan() {
		return new Head(20, THICKNESS, 100, 1);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, organ.getStartPoint());
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
	public void hasAPassNerve() {
		Nerve nerve = new Head(0, 0, 0, 1).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
}
