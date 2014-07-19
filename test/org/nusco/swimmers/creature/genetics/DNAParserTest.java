package org.nusco.swimmers.creature.genetics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DNAParserTest {

	@Test
	public void iteratesOverChromosomes() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5, 6,
				7, 8, 9, 10, 11, 12
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, parser.nextChromosome());
		assertArrayEquals(new int[]{7, 8, 9, 10, 11, 12}, parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test
	public void padsUnterminatedLastChromosome() {
		int[] genes =  new int[]{
				1, 2, 3, 4, 5, 6,
				7, 8
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, parser.nextChromosome());
		assertArrayEquals(new int[]{7, 8, 0, 0, 0, 0}, parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test
	public void padsUnterminatedSingleChromosome() {
		int[] genes =  new int[]{
				1, 2, 3
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test
	public void alwaysReturnsAtLeastOneChromosome() {
		int[] genes =  new int[0];

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{0, 0, 0, 0, 0, 0}, parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}
}
