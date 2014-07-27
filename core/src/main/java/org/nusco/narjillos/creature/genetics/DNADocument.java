package org.nusco.narjillos.creature.genetics;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

class DNADocument {

	private final String dnaDocument;

	public DNADocument(String dnaDocument) {
		this.dnaDocument = dnaDocument;
	}

	public Integer[] toGenes() {
		String[] lines = dnaDocument.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String cleanedUpLine = stripBraces(lines[i].trim());
			if (cleanedUpLine.matches("_*\\d.*"))
				return parseDNAString(cleanedUpLine);
		}
		throw new IllegalArgumentException("Illegal DNA syntax. At least one line must begin with a number.");
	}

	private String stripBraces(String line) {
		return line.replaceAll("[\\{\\}]", "_");
	}

	private Integer[] parseDNAString(String dnaString) {
		String[] numbers = dnaString.split("_");
		List<Integer> result = new LinkedList<>();
		try {
			for (int i = 0; i < numbers.length; i++)
				if (!numbers[i].isEmpty())
					result.add(Integer.parseInt(numbers[i]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal DNA syntax: " + dnaString);
		}
		return result.toArray(new Integer[result.size()]);
	}

	public static String toString(DNA dna) {
		StringBuffer result = new StringBuffer();
		DNAParser parser = new DNAParser(dna);
		int[] nextChromosome;
		while ((nextChromosome = parser.nextChromosome()) != null)
			result.append("{" + toString(nextChromosome) + "}");
		return result.toString();
	}

	private static String toString(int[] genes) {
		final DecimalFormat threeDigits = new DecimalFormat("000");
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < genes.length - 1; i++)
			result.append(threeDigits.format(genes[i]) + "_");
		result.append(threeDigits.format(genes[genes.length - 1]));
		return result.toString();
	}
}
