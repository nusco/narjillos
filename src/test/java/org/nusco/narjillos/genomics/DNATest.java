package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.NumGen;

public class DNATest {

	@Test
	public void hasASerialId() {
		var dna = new DNA(42, "{1_2_3}");

		assertThat(dna.getId()).isEqualTo(42);
	}

	@Test
	public void hasAnArrayOfGenes() {
		var dna = new DNA(1, "{1_2_255}");

		assertThat(dna.getGenes()).containsExactly(1, 2, 255);
	}

	@Test
	public void convertsToADNADocumentString() {
		var dna = new DNA(1, "1_2_3");

		assertThat(dna.toString()).isEqualTo("{001_002_003_000_000_000_000_000_000_000_000_000_000_000}");
	}

	@Test
	public void isNeverEmpty() {
		var dna = new DNA(1, "{}");

		assertThat(dna.toString()).isEqualTo("{000_000_000_000_000_000_000_000_000_000_000_000_000_000}");
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAnArray() {
		var dna = new DNA(1, "1_-1_256");

		assertThat(dna.getGenes()).containsExactly(1, 0, 255);
	}

	@Test
	public void clipsGenesToByteSizeWhenCreatedWithAString() {
		var dna = new DNA(1, "1_0_256");

		assertThat(dna.getGenes()).containsExactly(1, 0, 255);
	}

	@Test
	public void createsDNAFromADNADocumentString() {
		var dnaString = "comment\n" +
			"1_022_255";
		var dna = new DNA(1, dnaString);

		assertThat(dna.getGenes()).containsExactly(1, 22, 255);
	}

	@Test
	public void hasNoParentIdByDefault() {
		var dna = new DNA(42, "{1_2_3}");

		assertThat(dna.getParentId()).isEqualTo(DNA.NO_PARENT);
	}

	@Test
	public void canHaveAParentId() {
		var dna = new DNA(42, "{1_2_3}", 41);

		assertThat(dna.getParentId()).isEqualTo(41);
	}

	@Test
	public void knowsWetherItHasAParent() {
		assertThat(new DNA(42, "{1_2_3}", 41).hasParent()).isTrue();
		assertThat(new DNA(42, "{1_2_3}", 0).hasParent()).isFalse();
	}

	@Test
	public void splitsIntoCodons() {
		var dna = new DNA(1, "1_2_3_4_5_6_7_8_9_10_11_12");

		var expected = new Codon[]{
			new Codon(1, 2, 3),
			new Codon(4, 5, 6),
			new Codon(7, 8, 9),
			new Codon(10, 11, 12),
		};

		assertThat(dna.toCodons()).containsExactly(expected);
	}

	@Test
	public void padsCodonsWhenSplitting() {
		var dna = new DNA(1, "1_2_3_4_5_6_7_8_9_10_11_12_13");

		var expected = new Codon[]{
			new Codon(1, 2, 3),
			new Codon(4, 5, 6),
			new Codon(7, 8, 9),
			new Codon(10, 11, 12),
			new Codon(13, 0, 0)
		};

		assertThat(dna.toCodons()).containsExactly(expected);
	}

	@Test
	public void isEqualToAnotherDNAWithTheSameId() {
		assertThat(new DNA(1, "1_2_3")).isEqualTo(new DNA(1, "1_2_3"));
		assertThat(new DNA(1, "1_2_4")).isEqualTo(new DNA(1, "1_2_3"));
		assertThat(new DNA(2, "1_2_3")).isNotEqualTo(new DNA(1, "1_2_3"));
	}

	@Test
	public void givesRiseToAChildDNAWhenItMutates() {
		var parent = new DNA(42, "{}");
		var child = parent.mutate(43, new NumGen(123));

		assertThat(child.getParentId()).isEqualTo(42);
	}
}
