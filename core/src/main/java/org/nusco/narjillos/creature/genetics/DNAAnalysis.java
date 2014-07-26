package org.nusco.narjillos.creature.genetics;

public class DNAAnalysis {

	public static int getDistanceBetween(DNA dna1, DNA dna2) {
		Integer[] genes1 = dna1.getGenes();
		Integer[] genes2 = dna2.getGenes();

		if (genes1.length > genes2.length)
			return getDistanceBetween(dna2, dna1);
		
		int result = 0;
		for (int i = 0; i < genes1.length; i++)
			if (!genes1[i].equals(genes2[i]))
				result++;
		result += (genes2.length - genes1.length);
		return result;
	}
}
