package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DNAParserTest {

	@Test
	public void iteratesOverChromosomes() {
		Integer[] genes =  new Integer[]{
				1, 2, 3, 4, 5, 6, 7,
				8, 9, 10, 11, 12, 13, 14
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7), parser.nextChromosome());
		assertEquals(new Chromosome(8, 9, 10, 11, 12, 13, 14), parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test
	public void padsUnterminatedLastChromosome() {
		Integer[] genes =  new Integer[]{
				1, 2, 3, 4, 5, 6, 7,
				8, 9
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7), parser.nextChromosome());
		assertEquals(new Chromosome(8, 9, 0, 0, 0, 0, 0), parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test
	public void padsUnterminatedSingleChromosome() {
		Integer[] genes =  new Integer[]{
				1, 2, 3
			};

		DNA dna = new DNA(genes);
		DNAParser parser = new DNAParser(dna);
		
		assertEquals(new Chromosome(new int[]{1, 2, 3, 0, 0, 0}), parser.nextChromosome());
		assertNull(parser.nextChromosome());
	}

	@Test(expected=RuntimeException.class)
	public void throwsAnExceptionIfTheDNASequenceIsEmpty() {
		new DNA("{}");
	}
}
