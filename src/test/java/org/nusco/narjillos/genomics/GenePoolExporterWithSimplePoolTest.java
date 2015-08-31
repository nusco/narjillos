package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.persistence.VolatileDNALog;

public class GenePoolExporterWithSimplePoolTest {
	
	RanGen ranGen = new RanGen(1234);
	GenePool genePool = new SimpleGenePool(new VolatileDNALog());

	@Before
	public void setUpGenePool() {
		DNA dna1 = genePool.createRandomDNA(ranGen); // 1
		
		DNA dna2 = genePool.mutateDNA(dna1, ranGen); // 2
		genePool.mutateDNA(dna1, ranGen); // 3
		
		genePool.mutateDNA(dna2, ranGen); // 4
		
		DNA dna5 = genePool.createRandomDNA(ranGen); // 5
		genePool.mutateDNA(dna5, ranGen); // 6
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
