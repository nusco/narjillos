package org.nusco.swimmers.creature.genetics;

import java.util.Random;

import org.nusco.swimmers.shared.utilities.RanGen;

public class DNA {
	public static final int MIRROR_ORGAN = 0b00000001;
	public static final double MUTATION_RATE = 0.03;

	private int[] genes;

	public DNA(int... genes) {
		this.genes = normalize(genes);
	}

	private int[] normalize(int[] genes) {
		int[] result = new int[genes.length];
		for (int i = 0; i < genes.length; i++) {
			int normalized = genes[i] % 256;
			if(normalized < 0)
				result[i] = -normalized;
			else
				result[i] = normalized;
		}
		return result;
	}

	public int[] getGenes() {
		return genes;
	}

	public DNA mutate() {
		int[] resultGenes = new int[genes.length];
		for (int i = 0; i < resultGenes.length; i++) {
			if(RanGen.next() < MUTATION_RATE)
				resultGenes[i] = mutate(genes, i);
			else
				resultGenes[i] = genes[i];
		}
		return new DNA(resultGenes);
	}

	private int mutate(int[] resultGenes, int i) {
		int randomFactor = ((int)(RanGen.next() * 40)) - 20;
		return resultGenes[i] + randomFactor;
	}

	public static DNA random() {
		long seed = (long)(RanGen.next() * Long.MAX_VALUE);
		return random(seed);
	}
	
	public static DNA ancestor() {
		final long ancestorSeed = 9018779372573137080L;
		return DNA.random(ancestorSeed);
	}

	private static DNA random(long seed) {
		Random random = new Random(seed);
		final int genomeSize = 60 * Byte.SIZE;
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++)
			genes[i] = rnd(0, 255, random);
		return new DNA(genes);
	}

	private static int rnd(int min, int max, Random random) {
		return (int)(random.nextDouble() * (max - min)) + min;
	}
}
