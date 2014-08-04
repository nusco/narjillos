package org.nusco.narjillos.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WaveNerveTest {

	@Test
	public void generatesASinusWave() {
		final int LENGTH = 1;
		final double LENGTH_AT_45_DEGREES = 0.707;

		WaveNerve nerve = new WaveNerve(1.0 / 8);
		
		assertEquals(0, nerve.tick(0), 0.01);
		assertEquals(LENGTH_AT_45_DEGREES, nerve.tick(0), 0.01);
		assertEquals(LENGTH, nerve.tick(0), 0.01);

		// faster left semiplane
		assertEquals(0, nerve.tick(0), 0.01);
		assertEquals(-LENGTH, nerve.tick(0), 0.01);

		// back to right semiplane
		assertEquals(-LENGTH_AT_45_DEGREES, nerve.tick(0), 0.01);
		assertEquals(0, nerve.tick(0), 0.01);
	}
}
