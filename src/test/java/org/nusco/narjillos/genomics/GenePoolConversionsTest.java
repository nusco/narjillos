package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.RanGen;

public class GenePoolConversionsTest {
	
	RanGen ranGen = new RanGen(1234);
	GenePool genePool = new GenePool();

	@Before
	public void setUpGenePool() {
		genePool.enableAncestralMemory();
		DNA dna1 = genePool.createRandomDNA(ranGen); // 1
		
		DNA dna2 = genePool.mutateDNA(dna1, ranGen); // 2
		genePool.mutateDNA(dna1, ranGen); // 3
		
		genePool.mutateDNA(dna2, ranGen); // 4
		
		DNA dna5 = genePool.createRandomDNA(ranGen); // 5
		genePool.mutateDNA(dna5, ranGen); // 6
	}
	
	@Test
	public void canBeConvertedToCSVTree() {
		String expected = "0;1\n"
				+ "1;2\n"
				+ "1;3\n"
				+ "2;4\n"
				+ "0;5\n"
				+ "5;6\n";	
		assertEquals(expected, genePool.toCSVFormat());
	}
	
	@Test
	public void canBeConvertedToANEXUSTree() {
		String expected = "begin trees;\n" +
				"tree genotypes = (((4)2,3)1,(6)5)0;\n" + 
				"end;";	
		assertEquals(expected, genePool.toNEXUSFormat());
	}
}
