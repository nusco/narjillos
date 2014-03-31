package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.body.pns.PassNerve;

public class PassNerveTest {

	@Test
	public void readsOneByDefault() {
		Nerve nerve = new PassNerve();

		assertEquals(1.0, nerve.readOutputSignal(), 0);
	}

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNerve nerve = new PassNerve();

		nerve.send(3.0);
		assertEquals(3.0, nerve.readOutputSignal(), 0);

		nerve.send(42);
		assertEquals(42, nerve.readOutputSignal(), 0);
	}
}
