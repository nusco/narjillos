package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DNATest {

	@Test
	public void hasASerialId() {
		DNA dna = new DNA(42, "{1_2_3}");
		
		assertEquals(dna.getId(), 42);
	}

	@Test
	public void hasAnArrayOfGenes() {
		DNA dna = new DNA(1, "{1_2_255}");
		
		assertArrayEquals(new Integer[] {1, 2, 255}, dna.getGenes());
	}

	@Test
	public void isOnlyEqualIfItHasTheSameId() {
		DNA dna1 = new DNA(1, "{1_2_3}");
		DNA dna2 = new DNA(2, "{1_2_3}");
		DNA dna3 = new DNA(1, "{4_5_6}");
		
		assertFalse(dna1.equals(dna2));
		assertEquals(dna3, dna1);
	}

	@Test
	public void convertsToADNADocumentString() {
		DNA dna = new DNA(1, "1_2_3");
		
		assertEquals("{001_002_003_000_000_000_000_000_000_000_000_000}", dna.toString());
	}

	@Test
	public void isNeverEmpty() {
		DNA dna = new DNA(1, "{}");
		
		assertEquals("{000_000_000_000_000_000_000_000_000_000_000_000}", dna.toString());
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAnArray() {
		DNA dna = new DNA(1, "1_-1_256");
		
		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAString() {
		DNA dna = new DNA(1, "1_0_256");
		
		assertArrayEquals(new Integer[] {1, 0, 255}, dna.getGenes());
	}

	@Test
	public void createsDNAFromADNADocumentString() {
		String dnaString = "comment\n" +
							"1_022_255";
		DNA dna = new DNA(1, dnaString);

		assertArrayEquals(new Integer[] {1, 22, 255}, dna.getGenes());
	}

	@Test
	public void splitsIntoCodons() {
		DNA dna = new DNA(1, "1_2_3_4_5_6_7_8_9_10_11_12");

		Codon[] expected = new Codon[] {
				new Codon(1, 2, 3),
				new Codon(4, 5, 6),
				new Codon(7, 8, 9),
				new Codon(10, 11, 12),
		};
		assertArrayEquals(expected, dna.toCodons());
	}

	@Test
	public void padsCodonsWhenSplitting() {
		DNA dna = new DNA(1, "1_2_3_4_5_6_7_8_9_10_11_12_13");

		Codon[] expected = new Codon[] {
				new Codon(1, 2, 3),
				new Codon(4, 5, 6),
				new Codon(7, 8, 9),
				new Codon(10, 11, 12),
				new Codon(13, 0, 0)
		};
		assertArrayEquals(expected, dna.toCodons());
	}
}
