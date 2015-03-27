package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DNAAnalysisTest {

	@Test
	public void computesTheDistanceBetweenDNAStrands() {
		DNA dna1 = new DNA(1, "001_001_001");
		DNA dna2 = new DNA(2, "001_002_001");
		DNA dna3 = new DNA(3, "001_002_002");

		assertEquals(0, dna1.getLevenshteinDistanceFrom(dna1));
		assertEquals(1, dna1.getLevenshteinDistanceFrom(dna2));
		assertEquals(2, dna1.getLevenshteinDistanceFrom(dna3));
		assertEquals(1, dna2.getLevenshteinDistanceFrom(dna3));

		assertEquals(dna3.getLevenshteinDistanceFrom(dna1), dna1.getLevenshteinDistanceFrom(dna3));

		DNA dna4 = new DNA(4, "001_002");

		assertEquals(2, dna1.getLevenshteinDistanceFrom(dna4));
		assertEquals(2, dna4.getLevenshteinDistanceFrom(dna1));
	}
}
