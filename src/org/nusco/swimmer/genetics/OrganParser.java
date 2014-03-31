package org.nusco.swimmer.genetics;


class OrganParser {

	private final int[] genes;
	private int index_in_genes = 0;
	
	public OrganParser(DNA dna) {
		genes = dna.getGenes();
	}

	public int[] nextPart() {
		int[] result = new int[OrganBuilder.GENES_PER_PART];
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
