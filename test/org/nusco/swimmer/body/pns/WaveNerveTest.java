package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.physics.Vector;

public class WaveNerveTest extends NerveTest {
	
	@Test
	public void generatesASinusoidalWave() {
		assertWaveEquals(0.1, new double[] { 1.0, 0.809, 0.309, -0.309, -0.809, -1, -0.809, -0.309, 0.309, 0.809, 1 });
		assertWaveEquals(0.5, new double[] { 1, -1, 1 });
	}

	private void assertWaveEquals(double frequency, double[] expectedWave) {
		final double maxX = 10.0;
		final double maxY = 20.0;
		Vector inputSignal = new Vector(maxX, maxY);

		WaveNerve nerve = new WaveNerve(frequency);
		for (int i = 0; i < expectedWave.length; i++) {
			nerve.send(inputSignal);
			Vector outputSignal = nerve.readOutputSignal();
			
			double currentWave = expectedWave[i];
			Vector expected = new Vector(maxX * currentWave, maxY * currentWave);
			assertEquals(expected.getX(), outputSignal.getX(), 0.01);
			assertEquals(expected.getY(), outputSignal.getY(), 0.01);
		}
	}

	@Override
	protected Nerve createNerve() {
		return new WaveNerve(0.1);
	}
}
