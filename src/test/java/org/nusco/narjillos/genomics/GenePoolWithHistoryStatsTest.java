package org.nusco.narjillos.genomics;

import org.junit.Before;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.persistence.VolatileDNALog;

public class GenePoolWithHistoryStatsTest {
	
	NumGen numGen = new NumGen(1234);
	GenePool genePool = new GenePoolWithHistory(new VolatileDNALog());
	
	@Before
	public void setUpGenePool() {
		DNA dna1 = genePool.createRandomDNA(numGen); // 1
		
		DNA dna2 = genePool.mutateDNA(dna1, numGen); // 2
		genePool.mutateDNA(dna1, numGen); // 3
		
		genePool.mutateDNA(dna2, numGen); // 4
		
		DNA dna5 = genePool.createRandomDNA(numGen); // 5
		genePool.mutateDNA(dna5, numGen); // 6

		genePool.createRandomDNA(numGen); // 7

		genePool.remove(dna1); // remove 1
	}
}
