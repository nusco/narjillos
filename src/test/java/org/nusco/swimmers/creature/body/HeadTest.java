package org.nusco.swimmers.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.utilities.ColorByte;

public class HeadTest extends OrganTest {

	@Override
	public Head createConcreteBodyPart(int length, int thickness) {
		return new Head(length, thickness, new ColorByte(100), 1);
	}

	@Test
	public void startsAtPointZeroByDefault() {
		assertEquals(Vector.ZERO, part.getStartPoint());
	}

	@Override
	public void hasAnEndPoint() {
		assertEquals(Vector.cartesian(20, 0), part.getEndPoint());
	}

	@Override
	public void hasAParent() {
		assertEquals(null, part.getParent());
	}

	@Test
	public void hasAPassNerve() {
		Nerve nerve = new Head(0, 0, new ColorByte(0), 1).getNerve();
				
		assertEquals(PassNerve.class, nerve.getClass());
	}
}
