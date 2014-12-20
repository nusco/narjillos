package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.serializer.JSON;

/**
 * Check that two Experiments with the same seed result in the same exact
 * simulation running. This should be true even after an Experiment is
 * serialized and then deserialized.
 * 
 * To check whether that's true, we run two experiments for a few thousands
 * cycles and compare the results. This is not particularly safe, because bugs
 * with non-deterministic behavior may only become visible after many thousands
 * of cycles (especially because the experiment must have the time to see eggs
 * hatch, and narjillos grow enough to get some food). Every now and then, it's
 * worth trying this test with 10_000 cycles , just in case.
 */
public class DeterministicExperimentTest {

	private static final int CYCLES = 3_000;

	@Test
	public void experimentsAreDeterministic() {
		// Run an experiment for a few ticks
		Experiment experiment1 = new Experiment(1234, "deterministic_experiment_test");
		for (int i = 0; i < CYCLES; i++)
			experiment1.tick();

		// Serialize and deserialize the experiment.
		String json = JSON.toJson(experiment1, Experiment.class);
		Experiment experiment2 = JSON.fromJson(json, Experiment.class);

		// Now we have two experiments. Keep ticking both for a few more cycles.
		// Reset the DNA serial in between, so that we get the same DNA ids for
		// both experiments.
		long lastDNAId = DNA.getSerial();
		for (int i = 0; i < CYCLES; i++)
			experiment1.tick();
		DNA.setSerial(lastDNAId);
		for (int i = 0; i < CYCLES; i++)
			experiment2.tick();

		// Reset the running time (it's OK for it to differ between
		// experiments).
		experiment1.resetTotalRunningTime();
		experiment2.resetTotalRunningTime();

		// Compare the resulting experiments by serializing them to JSON and
		// stripping away any data that we expect to be different.
		String json1 = JSON.toJson(experiment1, Experiment.class);
		String json2 = JSON.toJson(experiment2, Experiment.class);
		assertEquals(json1, json2);
	}
}
