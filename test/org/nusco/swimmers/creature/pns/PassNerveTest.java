package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.physics.Vector;

public class PassNerveTest {

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		assertEquals(Vector.cartesian(3, 42), nerve.send(Vector.cartesian(3, 42)));
	}
}
