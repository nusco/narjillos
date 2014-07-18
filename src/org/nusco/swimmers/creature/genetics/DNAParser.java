package org.nusco.swimmers.creature.genetics;

class DNAParser {
	
	private final int[] genes;
	private int indexInGenes = 0;
	
	public DNAParser(DNA dna) {
		if (dna.getGenes().length > 0)
			genes = dna.getGenes();
		else
			genes = new int[1];
	}

	public int[] nextPart() {
		if (indexInGenes > 0 && indexInGenes >= genes.length)
			return null;
		
		int[] result = new int[DNA.GENES_PER_PART];
		int index_in_result = 0;
		while(index_in_result < result.length && indexInGenes < genes.length) {
			result[index_in_result] = genes[indexInGenes];
			index_in_result++;
			indexInGenes++;
		}
		return result;
	}
}
