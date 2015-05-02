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
		assertEquals(0, new Codon(0, 0, 0).hashCode(), 0.0);
		assertEquals(255, new Codon(255, 0, 0).hashCode(), 0.0);
		assertEquals(65535, new Codon(255, 255, 0).hashCode(), 0.0);
		assertEquals(Math.pow(2, 24) - 1, new Codon(255, 255, 255).hashCode(), 0.0);
	}
}
