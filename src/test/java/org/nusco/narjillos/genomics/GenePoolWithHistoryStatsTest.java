package org.nusco.narjillos.genomics;

import org.junit.Before;
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
}
