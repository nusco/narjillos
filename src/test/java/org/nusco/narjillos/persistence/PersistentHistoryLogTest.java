package org.nusco.narjillos.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;
import org.nusco.narjillos.experiment.SimpleExperiment;

public class PersistentHistoryLogTest {

	private PersistentHistoryLog historyLog;
	
	@Before
	public void createTestDababase() {
		historyLog = new PersistentHistoryLog("123-TESTING");
	}

	@After
	public void deleteTestDatabase() {
		historyLog.close();
		historyLog.delete();
	}
	
	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseFromMultiplePlaces() {
		PersistentHistoryLog logWithAnotherConnectionToTheSameDb = new PersistentHistoryLog("123-TESTING");
		logWithAnotherConnectionToTheSameDb.close();
	}
	
	@Test
	public void savesAndLoadsEntries() {
		PersistentHistoryLog historyLog = new PersistentHistoryLog("123-TESTING");

		Experiment experiment = new SimpleExperiment();
		
		for (int i = 0; i < 300; i++)
			experiment.tick();
		historyLog.saveEntries(experiment);
		for (int i = 0; i < 300; i++)
			experiment.tick();
		historyLog.saveEntries(experiment);

		ExperimentHistoryEntry latestStats = historyLog.getLatestEntry();

		assertNotNull(latestStats);
		assertEquals(new ExperimentHistoryEntry(experiment), latestStats);
	}
	
	@Test
	public void silentlySkipsWritingIfAnEntryIsAlreadyInTheDatabase() {
		PersistentHistoryLog historyLog = new PersistentHistoryLog("123-TESTING");

		Experiment experiment = new SimpleExperiment();

		for (int i = 0; i < 10; i++)
			experiment.tick();
		historyLog.saveEntries(experiment);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		historyLog.saveEntries(experiment);
		historyLog.saveEntries(experiment);

		List<ExperimentHistoryEntry> stats = historyLog.getEntries();

		assertEquals(2, stats.size());
	}
	
	@Test
	public void returnsNullIfThereAreNoEntriesInTheDatabase() {
		PersistentHistoryLog unknownDatabaseNameLog = new PersistentHistoryLog("unknown_experiment");
		try {
			assertNull(unknownDatabaseNameLog.getLatestEntry());
		} finally {
			unknownDatabaseNameLog.close();
			unknownDatabaseNameLog.delete();
		}
	}
}
