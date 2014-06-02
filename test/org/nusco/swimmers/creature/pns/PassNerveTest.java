package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.creature.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

public class PassNerveTest extends NerveTest {

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		nerve.send(Vector.cartesian(3, 42));
		assertEquals(Vector.cartesian(3, 42), nerve.getOutputSignal());
	}

	@Override
	protected Nerve createNerve() {
		return new PassNerve();
	}
}
