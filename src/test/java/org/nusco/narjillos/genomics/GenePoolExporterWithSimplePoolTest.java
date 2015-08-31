package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;

public class GenePoolExporterWithSimplePoolTest {
	
	NumGen numGen = new NumGen(1234);
	GenePool genePool = new SimpleGenePool();

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
	public void convertsASimpleGenePoolToAnEmptyCSVTree() {
		GenePoolExporter genePoolExporter = new GenePoolExporter(genePool);

		assertEquals("", genePoolExporter.toCSVFormat());
	}
	
	@Test
	public void convertsASimpleGenePoolToAnEmptyTree() {
		GenePoolExporter genePoolExporter = new GenePoolExporter(genePool);

		String expected = "begin trees;\n" +
				"tree genotypes = 0;\n" + 
				"end;";	
		assertEquals(expected, genePoolExporter.toNEXUSFormat());
	}
}
