package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DNAParserTest {

	@Test
	public void iteratesOverChromosomes() {
		Integer[] genes =  new Integer[]{
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
		Integer[] genes =  new Integer[]{
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
		Integer[] genes =  new Integer[]{
				1, 2, 3
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test(expected=RuntimeException.class)
	public void throwsAnExceptionIfTheDNASequenceIsEmpty() {
		new DNA("{}");
	}
}
