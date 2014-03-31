package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.body.pns.WaveNerve;

public class WaveNerveTest {

	@Test
	public void readsOneByDefault() {
		Nerve nerve = new WaveNerve();

		assertEquals(1.0, nerve.readOutputSignal(), 0);
	}

	@Test
	public void generatesASinusoidalWave() {
		WaveNerve nerve = new WaveNerve();

		double ticks[] = new double[CosWave.WAVE.length];
		for (int i = 0; i < ticks.length; i++) {
			nerve.send(CosWave.FREQUENCY);
			ticks[i] = nerve.readOutputSignal();
		}
		
		assertArrayEquals(CosWave.WAVE, ticks, CosWave.PRECISION);
	}
}
