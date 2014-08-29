package org.nusco.narjillos.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DelayNerveTest {

	@Test
	public void delaysASignal() {
		DelayNerve nerve = new DelayNerve(3);

		assertEquals(0, nerve.tick(1), 0);
		assertEquals(0, nerve.tick(2), 0);
		assertEquals(0, nerve.tick(3), 0);
		assertEquals(1, nerve.tick(4), 0);
		assertEquals(2, nerve.tick(5), 0);
		assertEquals(3, nerve.tick(6), 0);
	}

	@Test
	public void becomesAPassNerveWhenTheDelayIsZero() {
		DelayNerve nerve = new DelayNerve(0);

		assertEquals(1, nerve.tick(1), 0);
		assertEquals(2, nerve.tick(2), 0);
		assertEquals(3, nerve.tick(3), 0);
	}
}
