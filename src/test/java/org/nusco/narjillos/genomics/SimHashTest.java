package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SimHashTest {

	@Test
	public void calculatesBitDensity() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;

		var codons = new Codon[] {
			new Codon(gene1, gene1, gene2),
			new Codon(gene1, gene2, gene3),
			new Codon(gene2, gene3, gene3),
		};

		var expected = new int[] {
			-3, -3, -3, -3, -1, -1, -1, -1,
			-1, -1, -1, -1, 1, 1, 1, 1,
			1, 1, 1, 1, 3, 3, 3, 3
		};

		assertThat(SimHash.calculateBitDensity(codons)).containsExactly(expected);
	}

	@Test
	public void calculatesSimHash() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;

		var dna = new DNA(1, new Integer[] {
			gene1, gene1, gene2,
			gene1, gene2, gene3,
			gene2, gene3, gene3
		}, DNA.NO_PARENT);

		var expected = new int[] {
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 1, 1, 1, 1,
			1, 1, 1, 1, 1, 1, 1, 1
		};

		assertThat(SimHash.calculateSimHash(dna)).containsExactly(expected);
	}

	@Test
	public void calculatesLevenshteinDistanceBetweenDNAs() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;

		var dna1 = new DNA(1, new Integer[] { gene1, gene1, gene1 }, DNA.NO_PARENT);
		var dna2 = new DNA(1, new Integer[] { gene1, gene1, gene2 }, DNA.NO_PARENT);
		var dna3 = new DNA(1, new Integer[] { gene3, gene3, gene3 }, DNA.NO_PARENT);

		assertThat(dna1.getSimHashedDistanceFrom(dna1)).isEqualTo(0);
		assertThat(dna1.getSimHashedDistanceFrom(dna2)).isEqualTo(4);
		assertThat(dna1.getSimHashedDistanceFrom(dna3)).isEqualTo(24);
		assertThat(dna2.getSimHashedDistanceFrom(dna3)).isEqualTo(20);
	}
}
