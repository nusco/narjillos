package org.nusco.swimmers.creature.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.creature.pns.WaveNerve;
import org.nusco.swimmers.physics.Vector;

public class WaveNerveTest extends NerveTest {
	
	@Test
	public void generatesASinusoidalWave() {
		assertWaveEquals(0.1, new double[] { 1.0, 0.809, 0.309, -0.309, -0.809, -1, -0.809, -0.309, 0.309, 0.809, 1 });
		assertWaveEquals(0.5, new double[] { 1, -1, 1 });
	}

	private void assertWaveEquals(double frequency, double[] expectedWave) {
		final int angle = 15;
		Vector inputSignal = Vector.polar(angle, 1);

		WaveNerve nerve = new WaveNerve(frequency);
		for (int i = 0; i < expectedWave.length; i++) {
			nerve.send(inputSignal);
			Vector outputSignal = nerve.getOutputSignal();
			
			int expectedAngle = expectedWave[i] >= 0 ? angle : angle - 180;
			assertEquals(expectedAngle, outputSignal.getAngle(), 0.01);

			assertEquals(Math.abs(expectedWave[i]), outputSignal.getLength(), 0.01);
		}
	}

	@Override
	protected Nerve createNerve() {
		return new WaveNerve(0.1);
	}
}
