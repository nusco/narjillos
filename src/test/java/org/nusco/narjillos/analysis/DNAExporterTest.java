package org.nusco.narjillos.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class DNAExporterTest {

	private final NumGen numGen = new NumGen(1234);

	private final DNALog dnaLog = new VolatileDNALog();

	@Before
	public void setUpGenePool() {
		DNA dna1 = DNA.random(1, numGen);
		dnaLog.save(dna1);

		DNA dna2 = dna1.mutate(2, numGen);
		dnaLog.save(dna2);
		DNA dna3 = dna1.mutate(3, numGen);
		dnaLog.save(dna3);

		DNA dna4 = dna2.mutate(4, numGen);
		dnaLog.save(dna4);

		DNA dna5 = DNA.random(5, numGen);
		dnaLog.save(dna5);

		DNA dna6 = dna5.mutate(6, numGen);
		dnaLog.save(dna6);
	}

	@Test
	public void convertsAGenePoolToACSVTree() {
		DNAExporter dnaExporter = new DNAExporter(new DNAAnalyzer(dnaLog));

		String expected = """
                0;1
                1;2
                1;3
                2;4
                0;5
                5;6
                """;
		assertEquals(expected, dnaExporter.toCSVFormat());
	}

	@Test
	public void convertsAGenePoolToANEXUSTree() {
		DNAExporter dnaExporter = new DNAExporter(new DNAAnalyzer(dnaLog));

		String expected = """
                begin trees;
                tree genotypes = (((4)2,3)1,(6)5)0;
                end;""";
		assertEquals(expected, dnaExporter.toNEXUSFormat());
	}
}
