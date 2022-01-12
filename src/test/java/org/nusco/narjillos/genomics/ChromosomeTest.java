package org.nusco.narjillos.genomics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ChromosomeTest {

	@Test
	public void returnsASingleGene() {
		Chromosome chromosome = new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);

		assertThat(chromosome.getGene(3)).isEqualTo(4);
	}

	@Test
	public void padsMissingGenesWithZeroes() {
		var chromosome = new Chromosome(1, 2, 3, 4);

		var expected = new Chromosome(1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0);

		assertThat(chromosome).isEqualTo(expected);
	}

	@Test
	public void chokesOnNegativeGenes() {
		assertThatThrownBy(() -> new Chromosome(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void chokesOnGenesOver255() {
		assertThatThrownBy(() -> new Chromosome(1, 2, 3, 4, 5, 6, 256, 8, 9, 10, 11))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void convertsToAString() {
		var chromosome = new Chromosome(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);

		var expected = "{001_002_003_004_005_006_007_008_009_010_011_012_013_014}";

		assertThat(chromosome.toString()).isEqualTo(expected);
	}
}
