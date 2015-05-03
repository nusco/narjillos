package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimHashTest {

	@Test
	public void calculatesBitDensity() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;
		
		Codon[] codons = new Codon[] {
				new Codon(gene1, gene1, gene2),
				new Codon(gene1, gene2, gene3),
				new Codon(gene2, gene3, gene3),
		};
		
		int[] expected = new int[] {
				-3, -3, -3, -3, -1, -1, -1, -1,
				-1, -1, -1, -1, 1, 1, 1, 1,
				1, 1, 1, 1, 3, 3, 3, 3
		};
		
		assertArrayEquals(expected, SimHash.calculateBitDensity(codons));
	}
	
	@Test
	public void calculatesSimHash() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;

		DNA dna = new DNA(1, new Integer[] {
				gene1, gene1, gene2,
				gene1, gene2, gene3,
				gene2, gene3, gene3
		});
		
		int[] expected = new int[] {
				0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1
		};
		assertArrayEquals(expected, SimHash.calculateSimHash(dna));
	}

	@Test
	public void calculatesSimHashedDistanceBetweenDNAs() {
		int gene1 = 0b00000000;
		int gene2 = 0b00001111;
		int gene3 = 0b11111111;

		DNA dna1 = new DNA(1, new Integer[] {gene1, gene1, gene1});
		DNA dna2 = new DNA(1, new Integer[] {gene1, gene1, gene2});
		DNA dna3 = new DNA(1, new Integer[] {gene3, gene3, gene3});
		
		assertEquals(0, dna1.getSimHashedDistanceFrom(dna1));
		assertEquals(4, dna1.getSimHashedDistanceFrom(dna2));
		assertEquals(24, dna1.getSimHashedDistanceFrom(dna3));
		assertEquals(20, dna2.getSimHashedDistanceFrom(dna3));
	}
}
