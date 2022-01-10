package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DNADocumentTest {

	@Test
	public void createsGenesFromAConventionalString() {
		var dnaString = "1_022_255";
		var dnaDocument = new DNADocument(dnaString);

		assertThat(dnaDocument.toGenes()).containsExactly(1, 22, 255);
	}

	@Test
	public void ignoresSign() {
		var dnaDocument = new DNADocument("1_0__2");

		assertThat(dnaDocument.toGenes()).containsExactly(1, 0, 2);
	}

	@Test
	public void ignoresAnythingInTheDNAStringThatDoesntBeginWithANumber() {
		String dnaString = "comment 1\n" +
			"\n" +
			"1_0_255\n" +
			"comment 2\n" +
			"\n";
		var dnaDocument = new DNADocument(dnaString);

		assertThat(dnaDocument.toGenes()).containsExactly(1, 0, 255);
	}

	@Test
	public void ignoresAllCurlyBracesAndLeadingSpacesInTheGenes() {
		var dnaString = " {1_0}{255}{}";
		var dnaDocument = new DNADocument(dnaString);

		assertThat(dnaDocument.toGenes()).containsExactly(1, 0, 255);
	}

	@Test
	public void returnsTheZeroDNAIfNoLineBeginsWithANumber() {
		var dnaString = "comment\n";
		var dnaDocument = new DNADocument(dnaString);

		assertThat(dnaDocument.toGenes()).containsExactly(0);
	}

	@Test
	@Disabled
	public void throwsAnExceptionIfTheLineThatBeginsWithANumberIsMalformed() {
		var dnaString = "12-34";
		// var dnaDocument = new DNADocument(dnaString);

		assertThatThrownBy(() -> new DNADocument(dnaString))
			.isInstanceOf(IllegalArgumentException.class);

		// assertArrayEquals(new Integer[0], dnaDocument.toGenes());
	}

	@Test
	public void ignoresAnythingAfterTheGenesLine() {
		var dnaString = "comment\n" +
			" {1_2_3}\n" +
			"4_5_6_7_ignored_anyway";
		var dnaDocument = new DNADocument(dnaString);

		assertThat(dnaDocument.toGenes()).containsExactly(1, 2, 3);
	}

	@Test
	public void convertsDNAToADocumentString() {
		var dna = new DNA(1, "1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_17_18_19_20_21_22_23_24_25_26_27_28_255");

		var expected = "{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{015_016_017_018_019_020_021_022_023_024_025_026_027_028}" +
			"{255_000_000_000_000_000_000_000_000_000_000_000_000_000}";

		assertThat(dna.toString()).isEqualTo(expected);
	}
}
