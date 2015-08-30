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
import org.nusco.narjillos.genomics.DNA;

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

	@Test
	public void savesAndLoadsDNA() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		history.save(dna);
		DNA retrieved = history.getDNA(42);

		assertNotNull(retrieved);
		assertEquals(retrieved.getId(), dna.getId());
		assertEquals(retrieved.toString(), dna.toString());
		assertEquals(retrieved.getParentId(), dna.getParentId());
	}
	
	@Test
	public void returnsNullIfTheDNAIsNotInThePool() {
		assertNull(history.getDNA(42));
	}
	
	@Test
	public void retrievesTheEntireGenePool() {
		history.save(new DNA(42, "{1_2_3}", 0));
		history.save(new DNA(43, "{1_2_3}", 42));

		List<DNA> genePool = history.getAllDNA();
		
		assertEquals(2, genePool.size());
		assertEquals(history.getDNA(42), genePool.get(0));
		assertEquals(history.getDNA(43), genePool.get(1));
	}
	
	@Test
	public void silentlySkipsWritingIfADNAIsAlreadyInTheDatabase() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		history.save(dna);
		history.save(dna);

		assertEquals(1, history.getAllDNA().size());
	}
}
