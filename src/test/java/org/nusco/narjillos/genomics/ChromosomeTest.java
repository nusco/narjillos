package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChromosomeTest {

	@Test
	public void returnsASingleGene() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

		assertEquals(4, chromosome.getGene(3));
	}

	@Test
	public void padsMissingGenesWithZeroes() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4);

		Chromosome expected = new Chromosome(1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0);
		
		assertEquals(expected, chromosome);
	}

	@Test(expected=RuntimeException.class)
	public void chokesOnNegativeGenes() {
		new Chromosome(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11);
	}

	@Test(expected=RuntimeException.class)
	public void chokesOnGenesOver255() {
		new Chromosome(1, 2, 3, 4, 5, 6, 256, 8, 9, 10, 11);
	}

	@Test
	public void convertsToAString() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
		
		assertEquals("{001_002_003_004_005_006_007_008_009_010_011_012_013_014}", chromosome.toString());
	}
}
