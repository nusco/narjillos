package org.nusco.narjillos.genomics;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * A short sequence of genes in a long chain of DNA.
 */
public class Chromosome {

	static final int SIZE = 14;

	private final int[] genes = new int[SIZE];

	public Chromosome(int... genes) {
		for (int i = 0; i < genes.length; i++)
			if (genes[i] < 0 || genes[i] > 255)
				throw new RuntimeException("Invalid gene: " + genes[i]);
			else
				this.genes[i] = genes[i];
	}

	public int getGene(int index) {
		return genes[index];
	}

	@Override
	public String toString() {
		final DecimalFormat threeDigits = new DecimalFormat("000");
		StringBuilder result = new StringBuilder("{");
		for (int i = 0; i < genes.length - 1; i++)
			result.append(threeDigits.format(genes[i])).append("_");
		result.append(threeDigits.format(genes[genes.length - 1])).append("}");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(genes);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chromosome that = (Chromosome) o;
		return Arrays.equals(genes, that.genes);
	}
}
