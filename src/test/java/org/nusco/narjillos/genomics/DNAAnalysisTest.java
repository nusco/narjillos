package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DNAAnalysisTest {

	@Test
	public void computesTheDistanceBetweenTwoDNAStrands() {
		DNA dna = new DNA(1, "001_002_003");

		assertEquals(0, dna.getLevenshteinDistanceFrom(new DNA(2, "001_002_003")));
		assertEquals(1, dna.getLevenshteinDistanceFrom(new DNA(3, "001_004_003")));
		assertEquals(1, dna.getLevenshteinDistanceFrom(new DNA(4, "001_003")));
		assertEquals(1, dna.getLevenshteinDistanceFrom(new DNA(5, "001_002_004_003")));
		assertEquals(2, dna.getLevenshteinDistanceFrom(new DNA(6, "001_004_005")));
		assertEquals(3, dna.getLevenshteinDistanceFrom(new DNA(7, "004_005_006")));
	}
}
