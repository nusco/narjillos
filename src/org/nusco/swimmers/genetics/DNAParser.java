package org.nusco.swimmers.genetics;


public class DNAParser {

	private final int[] genes;
	private int index_in_genes = 0;
	
	public DNAParser(DNA dna) {
		genes = dna.getGenes();
	}

	public int[] next() {
		int[] result = new int[DNA.GENES_PER_PART];
		int index_in_result = 0;
		while(index_in_result < result.length) {
			if(index_in_genes >= genes.length)
				return result;
			if(isSkip(genes[index_in_genes])) {
				index_in_genes++;
				return result;
			}
			result[index_in_result] = genes[index_in_genes];
			index_in_result++;
			index_in_genes++;
		}
		return result;
	}

	private boolean isSkip(int b) {
		int maskedByte = b & 0b11000000;
		return maskedByte == DNA.TERMINATE_PART;
	}
}
