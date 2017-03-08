package org.nusco.narjillos.genomics;

import java.util.LinkedList;
import java.util.List;

/**
 * Converts a string to a DNA object.
 */
class DNADocument {

	private final String document;

	public DNADocument(String document) {
		this.document = document;
	}

	public Integer[] toGenes() {
		String[] lines = document.split("\n");
		for (String line : lines) {
			String cleanedUpLine = stripBraces(line.trim());
			if (cleanedUpLine.matches("_*\\d.*"))
				return parseDNAString(cleanedUpLine);
		}
		return new Integer[] { 0 };
	}

	public static String toString(DNA dna) {
		StringBuilder result = new StringBuilder();
		for (Chromosome chromosome : dna)
			result.append(chromosome.toString());
		return result.toString();
	}

	private String stripBraces(String line) {
		return line.replaceAll("[\\{\\}]", "_");
	}

	private Integer[] parseDNAString(String dnaString) {
		String[] numbers = dnaString.split("_");
		List<Integer> result = new LinkedList<>();
		try {
			for (String number : numbers)
				if (!number.isEmpty())
					result.add(Integer.parseInt(number));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Illegal DNA syntax: " + dnaString);
		}
		return result.toArray(new Integer[result.size()]);
	}
}
