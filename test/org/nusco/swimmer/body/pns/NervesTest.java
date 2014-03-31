package org.nusco.swimmer.body.pns;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.body.pns.DelayNerve;
import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.body.pns.PassNerve;

public class NervesTest {

	@Test
	public void canBeArrangedInATree() {
		Nerve inputNerve = new PassNerve();
		Nerve slowNerve = new DelayNerve(4);
		Nerve verySlowNerve = new DelayNerve(8);
		Nerve cascadedNerve = new DelayNerve(3);
		
		inputNerve.connectTo(slowNerve);
		inputNerve.connectTo(verySlowNerve);
		slowNerve.connectTo(cascadedNerve);

		inputNerve.send(0.10);
		assertSignal(1, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(1, cascadedNerve);

		inputNerve.send(0.20);
		assertSignal(1, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(1, cascadedNerve);

		inputNerve.send(0.30);
		assertSignal(1, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(1, cascadedNerve);

		inputNerve.send(0.40);
		assertSignal(0.10, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(1, cascadedNerve);

		inputNerve.send(0.50);
		assertSignal(0.20, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(1, cascadedNerve);

		inputNerve.send(0.60);
		assertSignal(0.30, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(0.10, cascadedNerve);

		inputNerve.send(0.70);
		assertSignal(0.40, slowNerve);
		assertSignal(1, verySlowNerve);
		assertSignal(0.20, cascadedNerve);

		inputNerve.send(0.80);
		assertSignal(0.50, slowNerve);
		assertSignal(0.10, verySlowNerve);
		assertSignal(0.30, cascadedNerve);
	}

	private void assertSignal(double expectedSignal, Nerve neuron) {
		assertEquals(expectedSignal, neuron.readOutputSignal(), CosWave.PRECISION);
	}
}
