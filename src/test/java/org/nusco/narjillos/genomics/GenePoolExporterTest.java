package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.persistence.VolatileDNALog;

public class GenePoolExporterTest {
	
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
	}
	
	@Test
	public void convertsAGenePoolToACSVTree() {
		GenePoolExporter genePoolExporter = new GenePoolExporter(genePool);

		String expected = "0;1\n"
				+ "1;2\n"
				+ "1;3\n"
				+ "2;4\n"
				+ "0;5\n"
				+ "5;6\n";	
		assertEquals(expected, genePoolExporter.toCSVFormat());
	}
	
	@Test
	public void convertsAGenePoolToANEXUSTree() {
		GenePoolExporter genePoolExporter = new GenePoolExporter(genePool);

		String expected = "begin trees;\n" +
				"tree genotypes = (((4)2,3)1,(6)5)0;\n" + 
				"end;";	
		assertEquals(expected, genePoolExporter.toNEXUSFormat());
	}
}
