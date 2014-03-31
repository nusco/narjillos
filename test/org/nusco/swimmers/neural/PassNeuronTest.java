package org.nusco.swimmers.neural;

import static org.junit.Assert.*;

import org.junit.Test;

public class PassNeuronTest {

	@Test
	public void readsOneByDefault() {
		Neuron neuron = new PassNeuron();

		assertEquals(1.0, neuron.readOutputSignal(), 0);
	}

	@Test
	public void passesASignalWithoutChangingIt() {
		PassNeuron neuron = new PassNeuron();

		neuron.send(3.0);
		assertEquals(3.0, neuron.readOutputSignal(), 0);

		neuron.send(42);
		assertEquals(42, neuron.readOutputSignal(), 0);
	}
}
