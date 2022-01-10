package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DNAAnalysisTest {

	@Test
	public void computesTheDistanceBetweenTwoDNAStrands() {
		var dna = new DNA(1, "001_002_003");

		assertThat(dna.getLevenshteinDistanceFrom(new DNA(2, "001_002_003"))).isEqualTo(0);
		assertThat(dna.getLevenshteinDistanceFrom(new DNA(3, "001_004_003"))).isEqualTo(1);
		assertThat(dna.getLevenshteinDistanceFrom(new DNA(4, "001_003"))).isEqualTo(1);
		assertThat(dna.getLevenshteinDistanceFrom(new DNA(5, "001_002_004_003"))).isEqualTo(1);
		assertThat(dna.getLevenshteinDistanceFrom(new DNA(6, "001_004_005"))).isEqualTo(2);
		assertThat(dna.getLevenshteinDistanceFrom(new DNA(7, "004_005_006"))).isEqualTo(3);
	}
}
