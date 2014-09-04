package org.nusco.narjillos.creature.genetics;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Chromosome {

	static final int SIZE = 8;
	
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
		StringBuffer result = new StringBuffer("{");
		for (int i = 0; i < genes.length - 1; i++)
			result.append(threeDigits.format(genes[i]) + "_");
		result.append(threeDigits.format(genes[genes.length - 1]) + "}");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(genes);
	}

	@Override
	public boolean equals(Object obj) {
		return Arrays.equals(genes, ((Chromosome) obj).genes);
	}
}
