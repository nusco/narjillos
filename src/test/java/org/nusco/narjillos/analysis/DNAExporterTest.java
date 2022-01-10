package org.nusco.narjillos.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class DNAExporterTest {

	private final NumGen numGen = new NumGen(1234);

	private final DNALog dnaLog = new VolatileDNALog();

	@BeforeEach
	public void setUpGenePool() {
		var dna1 = DNA.random(1, numGen);
		dnaLog.save(dna1);

		var dna2 = dna1.mutate(2, numGen);
		dnaLog.save(dna2);

		var dna3 = dna1.mutate(3, numGen);
		dnaLog.save(dna3);

		var dna4 = dna2.mutate(4, numGen);
		dnaLog.save(dna4);

		var dna5 = DNA.random(5, numGen);
		dnaLog.save(dna5);

		var dna6 = dna5.mutate(6, numGen);
		dnaLog.save(dna6);
	}

	@Test
	public void convertsAGenePoolToACSVTree() {
		var dnaExporter = new DNAExporter(new DNAAnalyzer(dnaLog));

		String expected = "0;1\n"
			+ "1;2\n"
			+ "1;3\n"
			+ "2;4\n"
			+ "0;5\n"
			+ "5;6\n";
		assertThat(dnaExporter.toCSVFormat()).isEqualTo(expected);
	}

	@Test
	public void convertsAGenePoolToANEXUSTree() {
		var dnaExporter = new DNAExporter(new DNAAnalyzer(dnaLog));

		String expected = "begin trees;\n" +
			"tree genotypes = (((4)2,3)1,(6)5)0;\n" +
			"end;";
		assertThat(dnaExporter.toNEXUSFormat()).isEqualTo(expected);
	}
}
