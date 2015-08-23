package org.nusco.narjillos.persistence.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.Stat;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;

public class DatabaseTest {

	private Database db;
	
	@Before
	public void createTestDababase() {
		db = new Database("123-TESTING");
	}

	@After
	public void deleteTestDatabase() {
		db.close();
		db.delete();
	}
	
	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseTwice() {
		Database anotherConnectionToTheSameDb = new Database("123-TESTING");
		anotherConnectionToTheSameDb.close();
	}
	
	@Test
	public void savesAndLoadsExperimentStats() {
		Experiment experiment = new Experiment(123, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "TESTING", false);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		db.updateStatsOf(experiment);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		db.updateStatsOf(experiment);

		Stat latestStats = db.getLatestStats();

		// FIXME
//		assertNotNull(latestStats);
//		assertEquals(new Stat(experiment), latestStats);
	}
	
	@Test
	public void returnsNullIfThereAreNoExperimentStatsInTheDatabase() {
		Database unknownExperimentDatabase = new Database("unknown_experiment");
		try {
			assertNull(unknownExperimentDatabase.getLatestStats());
		} finally {
			unknownExperimentDatabase.close();
			unknownExperimentDatabase.delete();
		}
	}

//	@Test
//	public void savesAndRetrievesDNA() {
//		DNA dna = new DNA(42, "{1_2_3}");
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
//	public void replacesIllegalDotsInDatabaseName() {
//		Database databaseWithIllegalName = new Database("1.2.3");
//		
//		assertEquals("1_2_3", databaseWithIllegalName.getName());
//	}
}
