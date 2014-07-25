package org.nusco.narjillos.creature.genetics;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.shared.utilities.RanGen;

public class DNA {

	private static final int MUTATION_RANGE = 30;
	public static final int CHROMOSOME_SIZE = 6;
	public static final double MUTATION_RATE = 0.1;

	private final Integer[] genes;

	public DNA(Integer[] genes) {
		this.genes = clipToByteSize(genes);
	}

	public DNA(String dnaString) {
		this(toGenes(dnaString));
	}

	private static Integer[] toGenes(String dnaString) {
		String[] lines = dnaString.split("\n");
		for (int i = 0; i < lines.length; i++)
			if (lines[i].matches("\\d.*"))
				return parseDNALine(lines[i]);
		throw new IllegalArgumentException("Illegal DNA syntax. At least one line must begin with a number.");
	}

	private static Integer[] parseDNALine(String dnaLine) {
		String[] numbers = dnaLine.split("-");
		List<Integer> result = new LinkedList<>();
		try {
			for (int i = 0; i < numbers.length; i++)
				if (!numbers[i].isEmpty())
					result.add(Integer.parseInt(numbers[i]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal DNA syntax: " + dnaLine);
		}
		return result.toArray(new Integer[result.size()]);
	}

	public Integer[] getGenes() {
		return genes;
	}

	public DNA mutate() {
		int length = Math.max(0, genes.length + getLengthMutation());
		Integer[] resultGenes = new Integer[length];
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

	private int mutate(Integer[] resultGenes, int i) {
		int randomFactor = ((int) (RanGen.nextDouble() * MUTATION_RANGE * 2)) - MUTATION_RANGE;
		return resultGenes[i] + randomFactor;
	}

	private int getLengthMutation() {
		if (RanGen.nextDouble() < MUTATION_RATE)
			return (int) (RanGen.nextGaussian() * CHROMOSOME_SIZE);

		return 0;
	}

	private Integer[] clipToByteSize(Integer[] genes) {
		Integer[] result = new Integer[genes.length];
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
		Integer[] genes = new Integer[genomeSize];
		for (int i = 0; i < genes.length; i++)
			genes[i] = RanGen.nextByte();
		return new DNA(genes);
	}

	@Override
	public String toString() {
		final DecimalFormat threeDigits = new DecimalFormat("000");
		StringBuffer result = new StringBuffer();
		int i;
		for (i = 0; i < genes.length - 1; i++)
			result.append(threeDigits.format(genes[i]) + "-");
		result.append(threeDigits.format(genes[i]));
		return result.toString();
	}
}
