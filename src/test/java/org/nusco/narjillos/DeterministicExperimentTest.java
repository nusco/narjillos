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
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;
import org.nusco.narjillos.persistence.serialization.JSON;

/**
 * Check that two Experiments with the same seed result in the same exact
 * simulation running. This should be true even after an Experiment is
 * serialized and then deserialized, so this test also checks that top-level
 * serialization works.
 * 
 * To check whether that's true, we run two experiments for about a thousands
 * cycles and compare the results. This is not particularly safe, because bugs
 * with non-deterministic behavior may only become visible after tens of
 * thousands of cycles (especially because the experiment must have the time to
 * see eggs hatch, and narjillos grow enough to get some food). Before releases,
 * it's worth running this test from the main() - that uses a much higher number
 * of cycles, but takes minutes even on a fast computer.
 */
public class DeterministicExperimentTest {

	private static PersistentDNALog genePoolLog1;
	private static PersistentDNALog genePoolLog2;
	private static PersistentHistoryLog historyLog1;
	private static PersistentHistoryLog historyLog2;

	// This test takes a few minutes. Run before packaging a release for
	// complete peace of mind.
	public static void main(String[] args) throws IOException {
		System.out.println("Running long deterministic experiment test. This will take a few minutes...");
		long startTime = System.currentTimeMillis();
		int cycles = 50_000;

		try {
			runTest(cycles);
		} catch (Throwable t) {
			throw t;
		} finally {
			deleteDatabases();
		}
		
		long totalTime = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("OK! (" + cycles + " cycles in " + totalTime + " seconds)");
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
		final int cycles = 1200;
		runTest(cycles);
	}

	public static void runTest(int cycles) throws IOException {
		int halfCycles = cycles / 2;

		// Run an experiment for a few ticks
		Experiment experiment1 = new Experiment(1234, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "deterministic_experiment_test");
		genePoolLog1 = new PersistentDNALog("test_database1");
		historyLog1 = new PersistentHistoryLog("test_database1");
		experiment1.setGenePool(new GenePool(genePoolLog1));
		experiment1.setHistoryLog(historyLog1);
		experiment1.populate();
		
		for (int i = 0; i < halfCycles; i++)
			experiment1.tick();

		// Serialize the experiment
		String json = JSON.toJson(experiment1, Experiment.class);
		
		// Copy the database
		genePoolLog1.close();
		historyLog1.close();
		copy("test_database1.history", "test_database2.history");
		genePoolLog1.open();
		historyLog1.open();

		// Deserialize the experiment
		Experiment experiment2 = JSON.fromJson(json, Experiment.class);
		genePoolLog2 = new PersistentDNALog("test_database2");
		historyLog2 = new PersistentHistoryLog("test_database2");
		experiment2.setGenePool(new GenePool(genePoolLog2));
		experiment2.setHistoryLog(historyLog2);

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

		// Decomment for quick debugging
		//		Files.write(new File("json1").toPath(), json1.getBytes());
		//		Files.write(new File("json2").toPath(), json2.getBytes());

		// Did it work?
		assertEquals(json1, json2);
	}

	private static void copy(String file1, String file2) throws IOException {
		File destinationFile = new File(file2);
		if (destinationFile.exists())
			Files.delete(destinationFile.toPath());
		Files.copy(new File(file1).toPath(), destinationFile.toPath());
	}
}
