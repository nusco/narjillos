package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.RanGen;

public class SimpleGenePoolStatsTest {
	
	RanGen ranGen = new RanGen(1234);
	GenePool genePool = new SimpleGenePool();
	GenePoolStats stats = new GenePoolStats(genePool);

	@Before
	public void setUpGenePool() {
		DNA dna1 = genePool.createRandomDNA(ranGen); // 1
		
		genePool.mutateDNA(dna1, ranGen); // 2
		genePool.mutateDNA(dna1, ranGen); // 3
	}
	
	@Test
	public void calculatesStats() {
		assertEquals(0, stats.getCurrentPoolSize());
		assertEquals(0, stats.getHistoricalPoolSize());
		assertEquals(0.0, stats.getAverageGeneration(), 0.0);
	}
}
