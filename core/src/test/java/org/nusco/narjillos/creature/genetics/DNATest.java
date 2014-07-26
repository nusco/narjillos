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
	public void clipsGenesToByteSizeWhenCreatedWithAString() {
		DNA dna = new DNA("1-0-256");
		
		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}

	@Test
	public void createsDNAFromADNADocumentString() {
		String dnaString = "comment\n" +
							"1-022-255";
		DNA dna = new DNA(dnaString);

		assertArrayEquals(new Integer[] {1, 22, 255}, dna.getGenes());
	}
}
