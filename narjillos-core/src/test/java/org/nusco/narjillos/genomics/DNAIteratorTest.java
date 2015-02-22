package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DNAIteratorTest {

	@Test
	public void iteratesOverChromosomes() {
		String genes = "{1_2_3_4_5_6_7_8_9_10_11}{12_13_14_15_16_17_18_19_20_21_22}";

		DNA dna = new DNA(1, genes);
		DNAIterator iterator = new DNAIterator(dna);
		
		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), iterator.nextChromosome());
		assertEquals(new Chromosome(12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22), iterator.nextChromosome());
		assertNull(iterator.nextChromosome());
	}

	@Test
	public void padsUnterminatedLastChromosome() {
		String genes = "{1_2_3_4_5_6_7_8_9_10_11}{12_13}";

		DNA dna = new DNA(1, genes);
		DNAIterator iterator = new DNAIterator(dna);
		
		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), iterator.nextChromosome());
		assertEquals(new Chromosome(12, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0), iterator.nextChromosome());
		assertNull(iterator.nextChromosome());
	}

	@Test
	public void padsUnterminatedSingleChromosome() {
		String genes = "{1_2_3}";

		DNA dna = new DNA(1, genes);
		DNAIterator iterator = new DNAIterator(dna);
		
		assertEquals(new Chromosome(new int[]{1, 2, 3, 0, 0, 0, 0}), iterator.nextChromosome());
		assertNull(iterator.nextChromosome());
	}

	public void alwaysReturnsAtLeastOneChromosome() {
		DNA dna = new DNA(1, "{}");
		DNAIterator iterator = new DNAIterator(dna);
		
		assertEquals(new Chromosome(new int[]{0, 0, 0, 0, 0, 0, 0}), iterator.nextChromosome());
		assertNull(iterator.nextChromosome());
	}
}
