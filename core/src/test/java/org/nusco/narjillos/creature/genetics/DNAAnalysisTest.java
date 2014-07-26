package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DNAAnalysisTest {

	@Test
	public void computesTheDistanceBetweenDNAStrands() {
		DNA dna1 = new DNA("001-001-001");
		DNA dna2 = new DNA("001-002-001");
		DNA dna3 = new DNA("001-002-002");

		assertEquals(0, dna1.getDistanceFrom(dna1));
		assertEquals(1, dna1.getDistanceFrom(dna2));
		assertEquals(2, dna1.getDistanceFrom(dna3));
		assertEquals(1, dna2.getDistanceFrom(dna3));

		assertEquals(dna3.getDistanceFrom(dna1), dna1.getDistanceFrom(dna3));

		DNA dna4 = new DNA("001-002");

		assertEquals(2, dna1.getDistanceFrom(dna4));
		assertEquals(2, dna4.getDistanceFrom(dna1));
	}
}
