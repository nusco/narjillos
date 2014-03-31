package org.nusco.swimmers.neural;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelayNeuronTest {

	@Test
	public void readsOneByDefault() {
		Neuron neuron = new DelayNeuron(4);

		assertEquals(1.0, neuron.readOutputSignal(), 0);
	}

	@Test
	public void delaysASignal() {
		DelayNeuron neuron = new DelayNeuron(3);

		assertEquals(1.0, neuron.process(0.1), CosWave.PRECISION);
		assertEquals(1.0, neuron.process(0.2), CosWave.PRECISION);
		assertEquals(0.1, neuron.process(0.3), CosWave.PRECISION);
		assertEquals(0.2, neuron.process(0.4), CosWave.PRECISION);
	}
}
