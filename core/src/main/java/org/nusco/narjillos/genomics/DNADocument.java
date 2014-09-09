package org.nusco.narjillos.genomics;

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
		throw new IllegalArgumentException("Illegal DNA syntax: no genes found.");
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
		Chromosome nextChromosome;
		while ((nextChromosome = parser.nextChromosome()) != null)
			result.append(nextChromosome.toString());
		return result.toString();
	}
}
