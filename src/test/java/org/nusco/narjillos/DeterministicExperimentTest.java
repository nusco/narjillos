package org.nusco.narjillos;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.AfterClass;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;
import org.nusco.narjillos.persistence.serialization.JSON;

/**
 * This test verifies that Experiments are deterministic: two experiments with
 * the same seed will result in the same exact simulation over time. To check
 * whether that's true, this test runs two experiments for thousands of cycles
 * and then compares the results, which should be identical.
 * <p/>
 * Due to the way the test works, it also verifies Experiment-level serialization.
 * (Experiments are the highest-level serialized objects in the system, so testing
 * experiments also tests all other serialized objects.) See the code for details.
 * <p/>
 * Note that when you run it in a JUnit-like runner, the test runs a quick ~1
 * Kcycles. That's not enough to catch all non-deterministic-behavior bugs, which
 * may only cause visible effects after tens of thousands of cycles - especially
 * considering that things tend to get more interesting after all eggs have hatched,
 * and narjillos have grown enough to get to food). Every now and then, it's worth
 * running this test from the main(), that uses around 50 Kcycles but takes minutes
 * even on a fast computer.
 * <p/>
 * This test has already caught quite a few subtle bugs already, so it proved
 * its value as one of the most important tests in the system.
 */
public class DeterministicExperimentTest {

	// Make true to print first experiment in case the test fails.
	private static final boolean DEBUG_PRINT_FIRST_EXPERIMENT_AS_JSON = false;

	// Same as above, but prints the second experiment.
	private static final boolean DEBUG_PRINT_SECOND_EXPERIMENT_AS_JSON = false;

	private static final int CYCLES_FOR_FAST_TEST = 1200;

	private static final int CYCLES_FOR_SAFE_TEST = 50_000;

	private static PersistentDNALog genePoolLog1;

	private static PersistentDNALog genePoolLog2;

	private static PersistentHistoryLog historyLog1;

	private static PersistentHistoryLog historyLog2;

	// This test takes a few minutes.
	// Run before packaging a release for complete peace of mind.
	public static void main(String[] args) throws IOException {
		System.out.println("Running long deterministic experiment test. This will take a few minutes...");
		long startTime = System.currentTimeMillis();

		try {
			runTest(CYCLES_FOR_SAFE_TEST, true);
		} catch (Throwable t) {
			throw t;
		} finally {
			deleteDatabases();
		}

		long totalTime = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("OK! (" + CYCLES_FOR_SAFE_TEST + " cycles in " + totalTime + " seconds)");
		System.exit(0); // otherwise Gradle won't exit (god knows why)
	}

	@AfterClass
	public static void deleteDatabases() throws IOException {
		genePoolLog1.delete();
		historyLog1.delete();
		genePoolLog2.delete();
		historyLog2.delete();
	}

	@Test
	public void experimentsAreDeterministic() throws IOException {
		runTest(CYCLES_FOR_FAST_TEST, false);
	}

	public static void runTest(int cycles, boolean showProgress) throws IOException {
		// Set up an experiment
		final int arbitrarySeed = 1234;
		final Ecosystem ecosystem = new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false);
		Experiment experiment1 = new Experiment(arbitrarySeed, ecosystem, "deterministic_experiment_test");
		genePoolLog1 = new PersistentDNALog("test_database1");
		historyLog1 = new PersistentHistoryLog("test_database1");
		experiment1.setDnaLog(genePoolLog1);
		experiment1.setHistoryLog(historyLog1);
		experiment1.populate();

		// Run the experiment for half the ticks
		final int halfCycles = cycles / 2;

		for (int cycle = 0; cycle < halfCycles; cycle++) {
			maybeShowProgress(showProgress, cycle, cycles);
			experiment1.tick();
		}

		// Serialize the experiment
		String json = JSON.toJson(experiment1, Experiment.class);

		// Copy the database, which includes the serialized experiment
		genePoolLog1.close();
		historyLog1.close();
		copy("test_database1.exp", "test_database2.exp");
		genePoolLog1.open();
		historyLog1.open();

		// Deserialize the experiment from the database copy
		Experiment experiment2 = JSON.fromJson(json, Experiment.class);
		genePoolLog2 = new PersistentDNALog("test_database2");
		historyLog2 = new PersistentHistoryLog("test_database2");
		experiment2.setDnaLog(genePoolLog2);
		experiment2.setHistoryLog(historyLog2);

		// Now that we have two experiments, keep ticking both for the remaining half of ticks
		for (int cycle = halfCycles; cycle < cycles; cycle++) {
			maybeShowProgress(showProgress, cycle, cycles);
			experiment1.tick();
			experiment2.tick();
		}

		// Reset the experiments' running time (which is meant for reporting and is
		// time-dependant, so it's OK if it differs between the two experiments)
		experiment1.resetTotalRunningTime();
		experiment2.resetTotalRunningTime();

		// Serialize both experiments as JSON so that they're easy to compare
		String json1 = JSON.toJson(experiment1, Experiment.class);
		String json2 = JSON.toJson(experiment2, Experiment.class);

		// Maybe print debugging info
		if (DEBUG_PRINT_FIRST_EXPERIMENT_AS_JSON)
			Files.write(new File("json1").toPath(), json1.getBytes());
		if (DEBUG_PRINT_SECOND_EXPERIMENT_AS_JSON)
			Files.write(new File("json2").toPath(), json2.getBytes());

		// Did it work?
		assertEquals(json1, json2);
	}

	private static void maybeShowProgress(boolean showProgress, int cycle, int totalCycles) {
		if (!showProgress)
			return;
		if (cycle % 1000 == 0)
			System.out.println(cycle + " of " + totalCycles);
	}

	private static void copy(String file1, String file2) throws IOException {
		File destinationFile = new File(file2);
		if (destinationFile.exists())
			Files.delete(destinationFile.toPath());
		Files.copy(new File(file1).toPath(), destinationFile.toPath());
	}
}
