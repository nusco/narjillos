package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.shared.utilities.RanGen;

public class DNA {

	public static final int CHROMOSOME_SIZE = 6;
	public static final double MUTATION_RATE = 0.1;

	private final int[] genes;

	public DNA(int... genes) {
		this.genes = genes;
	}

	public int[] getGenes() {
		return genes;
	}

	public DNA mutate() {
		int length = Math.max(0, genes.length + getLengthMutation());
		int[] resultGenes = new int[length];
		int copyLength = Math.min(resultGenes.length, genes.length);
		for (int i = 0; i < copyLength; i++) {
			if (RanGen.nextDouble() < MUTATION_RATE)
				resultGenes[i] = mutate(genes, i);
			else
				resultGenes[i] = genes[i];
		}
		for (int i = genes.length; i < resultGenes.length; i++)
			resultGenes[i] = randomByte();
		return new DNA(resultGenes);
	}

	private int getLengthMutation() {
		if (RanGen.nextDouble() < MUTATION_RATE)
			return (int) (RanGen.nextGaussian() * CHROMOSOME_SIZE);

		return 0;
	}

	private int mutate(int[] resultGenes, int i) {
		int randomFactor = ((int) (RanGen.nextDouble() * 40)) - 20;
		return clipToByteSize(resultGenes[i] + randomFactor);
	}

	private int clipToByteSize(int number) {
		if (number < 0)
			number = 0;
		if (number > Byte.MAX_VALUE)
			number = Byte.MAX_VALUE;
		return number;
	}

	public static DNA random() {
		final int genomeSize = CHROMOSOME_SIZE * (Math.abs(RanGen.nextInt()) % 15 + 10);
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++)
			genes[i] = randomByte();
		return new DNA(genes);
	}

	private static int randomByte() {
		return Math.abs(RanGen.nextInt()) % 255;
	}
}
