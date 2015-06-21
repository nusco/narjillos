package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.RanGen;

public class GenePoolWithHistoryStatsTest {
	
	RanGen ranGen = new RanGen(1234);
	GenePool genePool = new GenePoolWithHistory();
	
	@Before
	public void setUpGenePool() {
		DNA dna1 = genePool.createRandomDNA(ranGen); // 1
		
		DNA dna2 = genePool.mutateDNA(dna1, ranGen); // 2
		genePool.mutateDNA(dna1, ranGen); // 3
		
		genePool.mutateDNA(dna2, ranGen); // 4
		
		DNA dna5 = genePool.createRandomDNA(ranGen); // 5
		genePool.mutateDNA(dna5, ranGen); // 6

		genePool.createRandomDNA(ranGen); // 7

		genePool.remove(dna1); // remove 1
	}
	
	@Test
	public void calculatesStats() {
		GenePoolStats stats = new GenePoolStats(genePool);
		assertEquals(6, stats.getCurrentPoolSize());
		assertEquals(7, stats.getHistoricalPoolSize());
		assertEquals(1.83, stats.getAverageGeneration(), 0.01);
	}
	
	@Test
	public void convertsToACSVLine() {
		GenePoolStats stats = new GenePoolStats(genePool);

		assertEquals("6,7,1.83", stats.toCSVLine());
	}
}
