package org.nusco.swimmers.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

public class PassNerveTest {

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		assertEquals(Vector.cartesian(3, 42), nerve.tick(Vector.cartesian(3, 42)));
	}
}
