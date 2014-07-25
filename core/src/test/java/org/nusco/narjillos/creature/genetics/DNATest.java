package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DNATest {

	@Test
	public void isBasedOnAnArrayOfGenes() {
		DNA dna = new DNA(new Integer[] {1, 2, 255});
		
		assertArrayEquals(new Integer[] {1, 2, 255}, dna.getGenes());
	}

	@Test
	public void convertsToADescriptiveString() {
		DNA dna = new DNA(new Integer[] {1, 22, 255});
		
		assertEquals("001-022-255", dna.toString());
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAnArray() {
		DNA dna = new DNA(new Integer[] {1, -1, 256});
		
		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}
	
	@Test
	public void createsDNAFromAConventionalString() {
		String dnaString = 	"1-022-255";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[] {1, 22, 255}, dna.getGenes());
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAString() {
		DNA dna = new DNA("1-0-256");
		
		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}

	@Test
	public void ignoresSignWhenCreatedWithAString() {
		DNA dna = new DNA("1-0--2");
		
		assertArrayEquals(new Integer[] {1, 0, 2}, dna.getGenes());
	}

	@Test
	public void ignoresAnythingInTheDNAStringThatDoesntBeginWithANumber() {
		String dnaString = 	"comment 1\n" +
							"\n" +
							"1-0-255\n" +
							"comment 2\n" +
							"\n";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void throwsAnExceptionIfNoLineBeginsWithANumber() {
		String dnaString = 	"comment\n";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[0], dna.getGenes());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void throwsAnExceptionIfTheLineThatBeginsWithANumberIsMalformed() {
		String dnaString = 	"12_34";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[0], dna.getGenes());
	}
	
	@Test
	public void ignoresAnythingAfterTheFirstLineThatBeginsWithANumber() {
		String dnaString = 	"comment\n" + 
							"1-2-3\n" + 
							"4-5-6-ignored_anyway";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[] {1, 2, 3}, dna.getGenes());
	}
}
