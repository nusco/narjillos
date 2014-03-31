package org.nusco.swimmers.neural;

import static org.junit.Assert.*;

import org.junit.Test;

public class NeuronsTest {

	@Test
	public void canBeArrangedInATree() {
		Neuron inputNeuron = new PassNeuron();
		Neuron slowNeuron = new DelayNeuron(4);
		Neuron verySlowNeuron = new DelayNeuron(8);
		Neuron cascadedNeuron = new DelayNeuron(3);
		
		inputNeuron.connectTo(slowNeuron);
		inputNeuron.connectTo(verySlowNeuron);
		slowNeuron.connectTo(cascadedNeuron);

		inputNeuron.send(0.10);
		assertSignal(1, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(1, cascadedNeuron);

		inputNeuron.send(0.20);
		assertSignal(1, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(1, cascadedNeuron);

		inputNeuron.send(0.30);
		assertSignal(1, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(1, cascadedNeuron);

		inputNeuron.send(0.40);
		assertSignal(0.10, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(1, cascadedNeuron);

		inputNeuron.send(0.50);
		assertSignal(0.20, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(1, cascadedNeuron);

		inputNeuron.send(0.60);
		assertSignal(0.30, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(0.10, cascadedNeuron);

		inputNeuron.send(0.70);
		assertSignal(0.40, slowNeuron);
		assertSignal(1, verySlowNeuron);
		assertSignal(0.20, cascadedNeuron);

		inputNeuron.send(0.80);
		assertSignal(0.50, slowNeuron);
		assertSignal(0.10, verySlowNeuron);
		assertSignal(0.30, cascadedNeuron);
	}

	private void assertSignal(double expectedSignal, Neuron neuron) {
		assertEquals(expectedSignal, neuron.readOutputSignal(), CosWave.PRECISION);
	}
}
