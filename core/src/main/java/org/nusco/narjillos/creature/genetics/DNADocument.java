package org.nusco.narjillos.creature.genetics;

import java.util.LinkedList;
import java.util.List;

class DNADocument {

	private final String dnaDocument;

	public DNADocument(String dnaDocument) {
		this.dnaDocument = dnaDocument;
	}

	public Integer[] toGenes() {
		String[] lines = dnaDocument.split("\n");
		for (int i = 0; i < lines.length; i++)
			if (lines[i].matches("\\d.*"))
				return parseDNAString(lines[i]);
		throw new IllegalArgumentException("Illegal DNA syntax. At least one line must begin with a number.");
	}

	private Integer[] parseDNAString(String dnaString) {
		String[] numbers = dnaString.split("-");
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
}
