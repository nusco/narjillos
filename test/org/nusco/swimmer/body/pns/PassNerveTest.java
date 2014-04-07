package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

public class PassNerveTest extends NerveTest {

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		nerve.send(Vector.cartesian(3, 42));
		assertEquals(Vector.cartesian(3, 42), nerve.readOutputSignal());
	}

	@Override
	protected Nerve createNerve() {
		return new PassNerve();
	}
}
