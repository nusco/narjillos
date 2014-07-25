package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class DNATest {

	@Test
	public void isBasedOnAnArrayOfGenes() {
		DNA dna = new DNA(new int[] {1, 2, 255});
		
		assertArrayEquals(new int[] {1, 2, 255}, dna.getGenes());
	}

	@Test
	public void clipsGenesToByteSize() {
		DNA dna = new DNA(new int[] {1, -1, 256});
		
		assertArrayEquals(new int[] {1, 0, 255}, dna.getGenes());
	}
	
	@Test
	public void createsDNAFromAString() {
		String dnaString = 	"this is a DNA string\n" +
							"all these lines are comments\n" +
							"the line that starts with a # terminates the comments section\n" +
							"# and here it is... from now on, it's data\n" +
							"1\n" +
							"0\n" +
							"255\n";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new int[] {1, 0, 255}, dna.getGenes());
	}
	
	@Test
	public void acceptsStringsOfEmptyDNA() {
		String dnaString = 	"this string has an empty DNA\n" +
							"the separator is the last line in the string\n" +
							"# ...and here it is\n";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new int[0], dna.getGenes());
	}
	
	@Test
	public void returnsAnEmptyDNAIfTheDNAStringHasNoDataSeparator() {
		String dnaString = 	"no line begins with #\n" +
							"so this string is converting to an empty DNA\n";
		new DNA(dnaString);
	}
}
