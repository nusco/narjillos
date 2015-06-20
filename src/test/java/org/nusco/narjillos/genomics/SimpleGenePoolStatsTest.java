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
		
		DNA dna2 = genePool.mutateDNA(dna1, ranGen); // 2
		genePool.mutateDNA(dna1, ranGen); // 3
		
		genePool.mutateDNA(dna2, ranGen); // 4
		
		DNA dna5 = genePool.createRandomDNA(ranGen); // 5
		genePool.mutateDNA(dna5, ranGen); // 6

		genePool.remove(dna1); // remove 1
	}
	
	@Test
	public void calculatesStats() {
		assertEquals(0, stats.getCurrentPoolSize());
		assertEquals(0, stats.getHistoricalPoolSize());
	}
}
