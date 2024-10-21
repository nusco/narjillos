package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class DNAIterationTest {

	@Test
	public void iteratesOverChromosomes() {
		String genes = "{1_2_3_4_5_6_7_8_9_10_11_12_13_14}{15_16_17_18_19_20_21_22_23_24_25_26_27_28}";

		DNA dna = new DNA(1, genes);
		Iterator<Chromosome> iterator = dna.iterator();

		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14), iterator.next());
		assertEquals(new Chromosome(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void padsUnterminatedLastChromosome() {
		String genes = "{1_2_3_4_5_6_7_8_9_10_11_12_13_14}{15_16}";

		DNA dna = new DNA(1, genes);
		Iterator<Chromosome> iterator = dna.iterator();

		assertEquals(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14), iterator.next());
		assertEquals(new Chromosome(15, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void padsUnterminatedSingleChromosome() {
		String genes = "{1_2_3}";

		DNA dna = new DNA(1, genes);
		Iterator<Chromosome> iterator = dna.iterator();

		assertEquals(new Chromosome(1, 2, 3, 0, 0, 0, 0), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void alwaysReturnsAtLeastOneChromosome() {
		DNA dna = new DNA(1, "{}");

		Iterator<Chromosome> iterator = dna.iterator();

		assertTrue(iterator.hasNext());
		assertEquals(new Chromosome(0, 0, 0, 0, 0, 0, 0), iterator.next());
		assertFalse(iterator.hasNext());
	}
}
