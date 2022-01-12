package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class CodonTest {

	@Test
	public void isATripletOfGenes() {
		var codon = new Codon(10, 11, 12);

		assertThat(Codon.SIZE).isEqualTo(3);

		assertThat(codon.getGene(0)).isEqualTo(10);
		assertThat(codon.getGene(1)).isEqualTo(11);
		assertThat(codon.getGene(2)).isEqualTo(12);
	}

	@Test
	public void failsWithLessThanThreeGenes() {
		assertThatThrownBy(() -> new Codon(10, 11))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void failsWithMoreThanThreeGenes() {
		assertThatThrownBy(() -> new Codon(10, 11, 12, 13))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void hasA24BitHashCode() {
		assertThat(new Codon(0, 0, 0).hashCode()).isEqualTo(0b000000000000000000000000);
		assertThat(new Codon(1, 3, 7).hashCode()).isEqualTo(0b000000010000001100000111);
		assertThat(new Codon(255, 0, 0).hashCode()).isEqualTo(0b111111110000000000000000);
		assertThat(new Codon(255, 255, 0).hashCode()).isEqualTo(0b111111111111111100000000);
		assertThat(new Codon(255, 255, 255).hashCode()).isEqualTo(0b111111111111111111111111);
	}

	@Test
	public void convertsToAString() {
		assertThat(new Codon(0, 0, 0).toString()).isEqualTo("Codon:000000000000000000000000");
		assertThat(new Codon(1, 3, 7).toString()).isEqualTo("Codon:000000010000001100000111");
		assertThat(new Codon(255, 0, 255).toString()).isEqualTo("Codon:111111110000000011111111");
	}
}
