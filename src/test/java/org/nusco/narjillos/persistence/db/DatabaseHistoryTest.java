package org.nusco.narjillos.persistence.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class DatabaseHistoryTest {

	private DatabaseHistory history;
	
	@Before
	public void createDababaseHistory() {
		history = new DatabaseHistory("123-TESTING");
	}

	@After
	public void deleteTestDatabase() {
		history.close();
		history.delete();
	}
	
	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseFromMultiplePlaces() {
		History anotherConnectionToTheSameDb = new DatabaseHistory("123-TESTING");
		anotherConnectionToTheSameDb.close();
	}
	
	@Test
	public void savesAndLoadsStats() {
		Experiment experiment = new Experiment(123, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "TESTING");
		for (int i = 0; i < 300; i++)
			experiment.tick();
		history.saveStats(experiment);
		for (int i = 0; i < 300; i++)
			experiment.tick();
		history.saveStats(experiment);

		Stat latestStats = history.getLatestStats();

		assertNotNull(latestStats);
		assertEquals(new Stat(experiment), latestStats);
	}
	
	@Test
	public void silentlySkipsWritingIfAStatIsAlreadyInTheDatabase() {
		Experiment experiment = new Experiment(123, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "TESTING");
		for (int i = 0; i < 10; i++)
			experiment.tick();
		history.saveStats(experiment);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		history.saveStats(experiment);
		history.saveStats(experiment);

		List<Stat> stats = history.getStats();

		assertEquals(2, stats.size());
	}
	
	@Test
	public void returnsNullIfThereAreNoStatsInTheDatabase() {
		DatabaseHistory unknownExperimentDatabase = new DatabaseHistory("unknown_experiment");
		try {
			assertNull(unknownExperimentDatabase.getLatestStats());
		} finally {
			unknownExperimentDatabase.close();
			unknownExperimentDatabase.delete();
		}
	}

//	@Test
//	public void savesAndLoadsDNA() {
//		DNA dna = new DNA(42, "{1_2_3}");
//
//		
//		history.saveDNA(dna);
//
//		Stat latestStats = history.getLatestStats();
//
//		assertNotNull(latestStats);
//		assertEquals(new Stat(experiment), latestStats);
//
//		
//		db.newDNA(dna);
//		
//		DNA retrieved = db.getDNA(42);
//		assertEquals(42, retrieved.getId());
//		assertTrue(retrieved.toString().startsWith("{001_002_003_000"));
//	}
//	
//	@Test
//	public void returnsNullIfTheDNAIsNotInThePool() {
//		assertNull(db.getDNA(42));
//	}
//	
//	@Test
//	public void silentlySkipsWritingIfADNAIsAlreadyInTheDatabase() {
//		//TODO;
//	}
}
