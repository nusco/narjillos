package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodonTest {

	@Test
	public void isATripletOfGenes() {
		Codon codon = new Codon(10, 11, 12);

		assertEquals(3, Codon.SIZE);

		assertEquals(10, codon.getGene(0));
		assertEquals(11, codon.getGene(1));
		assertEquals(12, codon.getGene(2));
	}

	@Test(expected = RuntimeException.class)
	public void failsWithLessThanThreeGenes() {
		new Codon(10, 11);
	}

	@Test(expected = RuntimeException.class)
	public void failsWithMoreThanThreeGenes() {
		new Codon(10, 11, 12, 13);
	}

	@Test
	public void hasA24BitHashCode() {
		assertEquals(0b000000000000000000000000, new Codon(0, 0, 0).hashCode(), 0.0);
		assertEquals(0b000000010000001100000111, new Codon(1, 3, 7).hashCode(), 0.0);
		assertEquals(0b111111110000000000000000, new Codon(255, 0, 0).hashCode(), 0.0);
		assertEquals(0b111111111111111100000000, new Codon(255, 255, 0).hashCode(), 0.0);
		assertEquals(0b111111111111111111111111, new Codon(255, 255, 255).hashCode(), 0.0);
	}

	@Test
	public void convertsToAString() {
		assertEquals("Codon:000000000000000000000000", new Codon(0, 0, 0).toString());
		assertEquals("Codon:000000010000001100000111", new Codon(1, 3, 7).toString());
		assertEquals("Codon:111111110000000011111111", new Codon(255, 0, 255).toString());
	}
}
