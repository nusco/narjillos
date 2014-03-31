package org.nusco.swimmers.neural;

import static org.junit.Assert.*;

import org.junit.Test;

public class WaveNeuronTest {

	@Test
	public void readsOneByDefault() {
		Neuron neuron = new WaveNeuron();

		assertEquals(1.0, neuron.readOutputSignal(), 0);
	}

	@Test
	public void generatesASinusoidalWave() {
		WaveNeuron neuron = new WaveNeuron();

		double ticks[] = new double[CosWave.WAVE.length];
		for (int i = 0; i < ticks.length; i++) {
			neuron.send(CosWave.FREQUENCY);
			ticks[i] = neuron.readOutputSignal();
		}
		
		assertArrayEquals(CosWave.WAVE, ticks, CosWave.PRECISION);
	}
}
