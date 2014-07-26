package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class DNADocumentTest {

	@Test
	public void createsGenesFromAConventionalString() {
		String dnaString = 	"1-022-255";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 22, 255}, dnaDocument.toGenes());
	}

	@Test
	public void ignoresSign() {
		DNADocument dnaDocument = new DNADocument("1-0--2");
		
		assertArrayEquals(new Integer[] {1, 0, 2}, dnaDocument.toGenes());
	}

	@Test
	public void ignoresAnythingInTheDNAStringThatDoesntBeginWithANumber() {
		String dnaString = 	"comment 1\n" +
							"\n" +
							"1-0-255\n" +
							"comment 2\n" +
							"\n";
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
		String dnaString = 	"12_34";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[0], dnaDocument.toGenes());
	}
	
	@Test
	public void ignoresAnythingAfterTheFirstLineThatBeginsWithANumber() {
		String dnaString = 	"comment\n" + 
							"1-2-3\n" + 
							"4-5-6-ignored_anyway";
		DNADocument dnaDocument = new DNADocument(dnaString);

		assertArrayEquals(new Integer[] {1, 2, 3}, dnaDocument.toGenes());
	}
}
