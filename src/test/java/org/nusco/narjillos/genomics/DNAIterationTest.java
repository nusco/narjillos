package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

public class DNAIterationTest {

	@Test
	public void iteratesOverChromosomes() {
		var genes = "{1_2_3_4_5_6_7_8_9_10_11_12_13_14}{15_16_17_18_19_20_21_22_23_24_25_26_27_28}";
		var dna = new DNA(1, genes);

		Iterator<Chromosome> iterator = dna.iterator();

		assertThat(iterator.next()).isEqualTo(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
		assertThat(iterator.next()).isEqualTo(new Chromosome(15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28));
		assertThat(iterator.hasNext()).isFalse();
	}

	@Test
	public void padsUnterminatedLastChromosome() {
		var genes = "{1_2_3_4_5_6_7_8_9_10_11_12_13_14}{15_16}";
		var dna = new DNA(1, genes);

		Iterator<Chromosome> iterator = dna.iterator();

		assertThat(iterator.next()).isEqualTo(new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));
		assertThat(iterator.next()).isEqualTo(new Chromosome(15, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		assertThat(iterator.hasNext()).isFalse();
	}

	@Test
	public void padsUnterminatedSingleChromosome() {
		var genes = "{1_2_3}";
		var dna = new DNA(1, genes);

		Iterator<Chromosome> iterator = dna.iterator();

		assertThat(iterator.next()).isEqualTo(new Chromosome(1, 2, 3, 0, 0, 0, 0));
		assertThat(iterator.hasNext()).isFalse();
	}

	@Test
	public void alwaysReturnsAtLeastOneChromosome() {
		var dna = new DNA(1, "{}");

		Iterator<Chromosome> iterator = dna.iterator();

		assertThat(iterator.hasNext()).isTrue();
		assertThat(iterator.next()).isEqualTo(new Chromosome(0, 0, 0, 0, 0, 0, 0));
		assertThat(iterator.hasNext()).isFalse();
	}
}
