package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.utilities.Configuration;

/**
 * Check that two Experiments with the same seed result in the same exact
 * simulation running. This should be true even after an Experiment is
 * serialized and then deserialized.
 * 
 * To check whether that's true, we run two experiments for a few thousands
 * cycles and compare the results. This is not particularly safe, because bugs
 * with non-deterministic behavior may only become visible after tens of
 * thousands of cycles (especially because the experiment must have the time to
 * see eggs hatch, and narjillos grow enough to get some food). Before releases,
 * it's worth running this test from the main(), which uses a much higher number
 * of cycles, but takes minutes even on a fast computer.
 */
public class DeterministicExperimentTest {

	@Test
	public void experimentsAreDeterministic() {
		final int cycles = 1000;
		runTest(cycles);
	}

	public static void runTest(int cycles) {
		int halfCycles = cycles / 2;

		// Run an experiment for a few ticks
		Experiment experiment1 = new Experiment(1234, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "deterministic_experiment_test", true);

		for (int i = 0; i < halfCycles; i++)
			experiment1.tick();

		// Serialize and deserialize the experiment.
		String json = JSON.toJson(experiment1, Experiment.class);
		Experiment experiment2 = JSON.fromJson(json, Experiment.class);

		// Now we have two experiments. Keep ticking both for a few more cycles.
		for (int i = 0; i < halfCycles; i++) {
			experiment1.tick();
			experiment2.tick();
		}

		// Reset the running time (which is generally different between
		// experiments, and that's OK).
		experiment1.resetTotalRunningTime();
		experiment2.resetTotalRunningTime();

		// Compare the resulting experiments by serializing them to JSON and
		// stripping away any data that we expect to be different.
		String json1 = JSON.toJson(experiment1, Experiment.class);
		String json2 = JSON.toJson(experiment2, Experiment.class);
		assertEquals(json1, json2);
	}

	// This test takes almost 10 minutes. Run before packaging a release for
	// complete peace of mind.
	public static void main(String[] args) {
		System.out.println("Running long deterministic experiment test. This will take a few minutes...");
		long startTime = System.currentTimeMillis();
		int cycles = 30_000;

		runTest(cycles);

		long totalTime = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("OK! (" + cycles + " cycles in " + totalTime + " seconds)");
		System.exit(0); // otherwise Gradle won't exit (god knows why)
	}
}
