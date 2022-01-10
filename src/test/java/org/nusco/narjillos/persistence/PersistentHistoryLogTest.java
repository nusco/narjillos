package org.nusco.narjillos.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;
import org.nusco.narjillos.experiment.SimpleExperiment;

public class PersistentHistoryLogTest {

	private PersistentHistoryLog historyLog;

	@BeforeEach
	public void createTestDababase() {
		historyLog = new PersistentHistoryLog("123-TESTING");
	}

	@AfterEach
	public void deleteTestDatabase() {
		historyLog.close();
		historyLog.delete();
	}

	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseFromMultiplePlaces() {
		var logWithAnotherConnectionToTheSameDb = new PersistentHistoryLog("123-TESTING");
		logWithAnotherConnectionToTheSameDb.close();
	}

	@Test
	public void savesAndLoadsEntries() {
		var historyLog = new PersistentHistoryLog("123-TESTING");

		Experiment experiment = new SimpleExperiment();

		for (int i = 0; i < 300; i++)
			experiment.tick();
		historyLog.saveEntry(experiment);
		for (int i = 0; i < 300; i++)
			experiment.tick();
		historyLog.saveEntry(experiment);

		ExperimentHistoryEntry latestStats = historyLog.getLatestEntry();

		assertThat(latestStats).isNotNull();
		assertThat(latestStats).isEqualTo(new ExperimentHistoryEntry(experiment));
	}

	@Test
	public void silentlySkipsWritingIfAnEntryIsAlreadyInTheDatabase() {
		var historyLog = new PersistentHistoryLog("123-TESTING");

		Experiment experiment = new SimpleExperiment();

		for (int i = 0; i < 10; i++)
			experiment.tick();
		historyLog.saveEntry(experiment);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		historyLog.saveEntry(experiment);
		historyLog.saveEntry(experiment);

		List<ExperimentHistoryEntry> stats = historyLog.getEntries();

		assertThat(stats).hasSize(2);
	}

	@Test
	public void returnsNullIfThereAreNoEntriesInTheDatabase() {
		var unknownDatabaseNameLog = new PersistentHistoryLog("unknown_experiment");
		try {
			assertThat(unknownDatabaseNameLog.getLatestEntry()).isNull();
		} finally {
			unknownDatabaseNameLog.close();
			unknownDatabaseNameLog.delete();
		}
	}
}
