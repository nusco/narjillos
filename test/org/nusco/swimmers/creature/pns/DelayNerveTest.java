package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.DelayNerve;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public class DelayNerveTest extends NerveTest {

	@Test
	public void delaysASignal() {
		DelayNerve nerve = new DelayNerve(3);

		Vector vector1 = Vector.cartesian(0.1, 0);
		Vector vector2 = Vector.cartesian(0.2, 0);
		Vector vector3 = Vector.cartesian(0.3, 0);
		Vector vector4 = Vector.cartesian(0.4, 0);

		assertEquals(Vector.ZERO_ONE, nerve.process(vector1));
		assertEquals(Vector.ZERO_ONE, nerve.process(vector2));
		assertEquals(vector1, nerve.process(vector3));
		assertEquals(vector2, nerve.process(vector4));
	}

	@Override
	protected Nerve createNerve() {
		return new DelayNerve(3);
	}
}
