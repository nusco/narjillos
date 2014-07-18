package org.nusco.swimmers.creature.genetics;

import java.util.Random;

import org.nusco.swimmers.shared.utilities.RanGen;

public class DNA {

	public static final int GENES_PER_PART = 5;
	public static final double MUTATION_RATE = 0.03;

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
		return new DNA(resultGenes);
	}

	private int getLengthMutation() {
		if (RanGen.nextDouble() < MUTATION_RATE * 3)
			return (int) (RanGen.nextGaussian() * GENES_PER_PART * 2);

		return 0;
	}

	private int mutate(int[] resultGenes, int i) {
		int randomFactor = ((int) (RanGen.nextDouble() * 40)) - 20;
		return resultGenes[i] + randomFactor;
	}

	public static DNA ancestor() {
		final long ancestorSeed = 9018779372573137080L;
		return DNA.random(ancestorSeed);
	}

	public static DNA random() {
		long seed = (long) (RanGen.nextDouble() * Long.MAX_VALUE);
		return random(seed);
	}

	private static DNA random(long seed) {
		Random random = new Random(seed);
		final int genomeSize = GENES_PER_PART * (Math.abs(random.nextInt()) % 6 + 2);
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++)
			genes[i] = randomByte(random);
		return new DNA(genes);
	}

	private static int randomByte(Random random) {
		return Math.abs(random.nextInt()) % 255;
	}
}
