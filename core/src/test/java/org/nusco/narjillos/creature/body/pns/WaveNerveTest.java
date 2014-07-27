package org.nusco.narjillos.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WaveNerveTest {

	@Test
	public void generatesASinusWave() {
		final int length = 1;
		final double lengthAt45Degrees = 0.707;

		WaveNerve nerve = new WaveNerve(1.0 / 8);
		assertEquals(0, nerve.tick(0), 0.01);
		assertEquals(-lengthAt45Degrees, nerve.tick(0), 0.01);
		assertEquals(-length, nerve.tick(0), 0.01);
		assertEquals(-lengthAt45Degrees, nerve.tick(0), 0.01);

		// on the positive side, the wave is twice as fast
		assertEquals(lengthAt45Degrees, nerve.tick(0), 0.01);
		assertEquals(lengthAt45Degrees, nerve.tick(0), 0.01);
		assertEquals(0, nerve.tick(0), 0.01);
	}

	@Test
	public void skewesTheWaveWithANonZeroInput() {
		final int length = 1;
		final double lengthAt45Degrees = 0.707;

		WaveNerve nerve = new WaveNerve(1.0 / 8);
		int skew = 1;

		assertEquals(skew, nerve.tick(skew), 0.01);
		assertEquals(skew - lengthAt45Degrees, nerve.tick(skew), 0.01);
		assertEquals(skew - length, nerve.tick(skew), 0.01);
		assertEquals(skew - lengthAt45Degrees, nerve.tick(skew), 0.01);

		// on the positive side, the wave is twice as fast
		assertEquals(skew + lengthAt45Degrees, nerve.tick(skew), 0.01);
		assertEquals(skew + lengthAt45Degrees, nerve.tick(skew), 0.01);
		assertEquals(skew, nerve.tick(skew), 0.01);
	}
}
