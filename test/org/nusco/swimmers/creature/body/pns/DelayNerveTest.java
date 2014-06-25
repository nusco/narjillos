package org.nusco.swimmers.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.DelayNerve;
import org.nusco.swimmers.physics.Vector;

public class DelayNerveTest {

	@Test
	public void delaysASignal() {
		DelayNerve nerve = new DelayNerve(3);

		Vector vector1 = Vector.cartesian(0.1, 0);
		Vector vector2 = Vector.cartesian(0.2, 0);
		Vector vector3 = Vector.cartesian(0.3, 0);
		Vector vector4 = Vector.cartesian(0.4, 0);

		assertEquals(vector1, nerve.tick(vector1));
		assertEquals(vector1, nerve.tick(vector2));
		assertEquals(vector1, nerve.tick(vector3));
		assertEquals(vector2, nerve.tick(vector4));
	}
}
