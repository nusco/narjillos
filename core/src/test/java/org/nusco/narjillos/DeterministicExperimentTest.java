package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.serializer.JSON;

public class DeterministicExperimentTest {

	// Check that two Experiments with the same seed result in the
	// same exact simulation running. This should be true even after
	// an Experiment is serialized and then deserialized.
	// To check whether that's true, we run two experiments for 100
	// cycles. This is good for test speed, but not particularly safe
	// - many bugs with non-deterministic behavior only become apparent
	// after 1000 or even 10_000 cycles. Every now and then, it's
	// worth trying this with a higher number of cycles , just in case.
	private static final int CYCLES = 100;

	@Test
	public void isDeterministic() {
		Experiment experiment = new Experiment("x", "1234");
		for (int i = 0; i < CYCLES; i++)
			experiment.tick();
		
		String json = JSON.toJson(experiment, Experiment.class);
		Experiment deserialized = JSON.fromJson(json, Experiment.class);

		for (int i = 0; i < CYCLES; i++) {
			experiment.tick();
			deserialized.tick();
		}

		assertEquals(experiment.getEcosystem().getNumberOfFoodPieces(), deserialized.getEcosystem().getNumberOfFoodPieces());
		assertEquals(experiment.getEcosystem().getNumberOfNarjillos(), deserialized.getEcosystem().getNumberOfNarjillos());
	}
}
