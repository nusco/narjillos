package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.DelayNerve;
import org.nusco.swimmer.body.pns.Nerve;

public class DelayNerveTest {

	@Test
	public void readsOneByDefault() {
		Nerve nerve = new DelayNerve(4);

		assertEquals(1.0, nerve.readOutputSignal(), 0);
	}

	@Test
	public void delaysASignal() {
		DelayNerve nerve = new DelayNerve(3);

		assertEquals(1.0, nerve.process(0.1), CosWave.PRECISION);
		assertEquals(1.0, nerve.process(0.2), CosWave.PRECISION);
		assertEquals(0.1, nerve.process(0.3), CosWave.PRECISION);
		assertEquals(0.2, nerve.process(0.4), CosWave.PRECISION);
	}
}
