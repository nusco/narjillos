package org.nusco.narjillos.creature.genetics;

public class DNAParser {
	
	private final Integer[] genes;
	private int indexInGenes = 0;
	
	public DNAParser(DNA dna) {
		if (dna.getGenes().length > 0)
			genes = dna.getGenes();
		else
			// FIXME: throw an exception for now, but this is not enough.
			// I should probably abort any creature that doesn't have two
			// chromosomes min.
			throw new RuntimeException("Empty DNA");
//			genes = new Integer[] { 0 };
	}

	public Chromosome nextChromosome() {
		if (indexInGenes > 0 && indexInGenes >= genes.length)
			return null;
		
		int[] result = new int[DNA.CHROMOSOME_SIZE];
		int index_in_result = 0;
		while(index_in_result < result.length && indexInGenes < genes.length) {
			result[index_in_result] = genes[indexInGenes];
			index_in_result++;
			indexInGenes++;
		}
		return new Chromosome(result);
	}
}
