package org.nusco.swimmers.creature.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmers.creature.body.pns.WaveNerve;
import org.nusco.swimmers.physics.Vector;

public class WaveNerveTest {
	
	@Test
	public void generatesASinusoidalWaveBasedOnTheInputSign() {
		assertWaveEquals(0.1, Vector.polar(15, 1), new double[] { 10.0, 8.09, 3.09, -3.09, -8.09, -10, -8.09, -3.09, 3.09, 8.09, 10 });
		assertWaveEquals(0.5, Vector.polar(15, 10), new double[] { 100, -100, 100 });
	}

	private void assertWaveEquals(double frequency, Vector inputSignal, double[] expectedWave) {
		double angle = inputSignal.getAngle();
		
		WaveNerve nerve = new WaveNerve(frequency);
		for (int i = 0; i < expectedWave.length; i++) {
			Vector outputSignal = nerve.send(inputSignal);
			
			double expectedAngle = expectedWave[i] >= 0 ? angle - 90 : angle + 90;
			assertEquals(expectedAngle, outputSignal.getAngle(), 0.01);

			assertEquals(Math.abs(expectedWave[i]), outputSignal.getLength(), 0.01);
		}
	}
}
