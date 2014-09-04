package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DNADocumentTest {

	@Test
	public void createsGenesFromAConventionalString() {
		String dnaString = 	"1_022_255";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 22, 255}, dnaDocument.toGenes());
	}

	@Test
	public void ignoresSign() {
		DNADocument dnaDocument = new DNADocument("1_0__2");
		
		assertArrayEquals(new Integer[] {1, 0, 2}, dnaDocument.toGenes());
	}

	@Test
	public void ignoresAnythingInTheDNAStringThatDoesntBeginWithANumber() {
		String dnaString = 	"comment 1\n" +
							"\n" +
							"1_0_255\n" +
							"comment 2\n" +
							"\n";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 0, 255}, dnaDocument.toGenes());
	}

	@Test
	public void ignoresAllCurlyBracesAndLeadingSpacesInTheGenes() {
		String dnaString = 	" {1_0}{255}{}";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 0, 255}, dnaDocument.toGenes());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void throwsAnExceptionIfNoLineBeginsWithANumber() {
		String dnaString = 	"comment\n";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[0], dnaDocument.toGenes());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void throwsAnExceptionIfTheLineThatBeginsWithANumberIsMalformed() {
		String dnaString = 	"12-34";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[0], dnaDocument.toGenes());
	}
	
	@Test
	public void ignoresAnythingAfterTheGenesLine() {
		String dnaString = 	"comment\n" + 
							" {1_2_3}\n" + 
							"4_5_6_7_ignored_anyway";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 2, 3}, dnaDocument.toGenes());
	}

	@Test
	public void convertsDNAToADocumentString() {
		DNA dna = new DNA("1_2_3_4_5_6_7_8_9_10_11_12_13_14_15_16_255");
		
		assertEquals("{001_002_003_004_005_006_007_008}{009_010_011_012_013_014_015_016}{255_000_000_000_000_000_000_000}", dna.toString());
	}
}
