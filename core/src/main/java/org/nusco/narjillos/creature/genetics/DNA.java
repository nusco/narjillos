package org.nusco.narjillos.creature.genetics;

import java.util.Arrays;

import org.nusco.narjillos.shared.utilities.RanGen;

public class DNA {

	private static final int MUTATION_RANGE = 30;
	public static final int CHROMOSOME_SIZE = 6;
	public static final double MUTATION_RATE = 0.1;

	private final int[] genes;

	public DNA(int... genes) {
		this.genes = clipToByteSize(genes);
	}

	public DNA(String dnaString) {
		this(toGenes(dnaString));
	}

	private static int[] toGenes(String dnaString) {
		String[] lines = dnaString.split("\n");
		int geneLine = getIndexOfFirstGeneLine(lines);

		if (geneLine >= lines.length)
			return new int[0];
		int[] genes = new int[lines.length - geneLine];
		
		for (int i = 0; i < genes.length; i++) {
			String nextLine = lines[geneLine + i];
			genes[i] = Integer.parseInt(nextLine);
		}
		
		return genes;
	}

	private static int getIndexOfFirstGeneLine(String[] lines) {
		int result = 0;
		
		while (result < lines.length && !lines[result].startsWith("#"))
			result++;
		
		return result + 1;
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
			resultGenes[i] = RanGen.nextByte();
		return new DNA(resultGenes);
	}

	private int mutate(int[] resultGenes, int i) {
		int randomFactor = ((int) (RanGen.nextDouble() * MUTATION_RANGE * 2)) - MUTATION_RANGE;
		return resultGenes[i] + randomFactor;
	}

	private int getLengthMutation() {
		if (RanGen.nextDouble() < MUTATION_RATE)
			return (int) (RanGen.nextGaussian() * CHROMOSOME_SIZE);

		return 0;
	}

	private int[] clipToByteSize(int... genes) {
		int[] result = new int[genes.length];
		for (int i = 0; i < result.length; i++)
			result[i] = clipToByteSize(genes[i]);
		return result;
	}

	private int clipToByteSize(int number) {
		if (number < 0)
			number = 0;
		if (number > 255)
			number = 255;
		return number;
	}

	public static DNA random() {
		final int genomeSize = CHROMOSOME_SIZE * (Math.abs(RanGen.nextInt()) % 10 + 2);
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++)
			genes[i] = RanGen.nextByte();
		return new DNA(genes);
	}

	@Override
	public String toString() {
		return Arrays.toString(genes);
	}
}
