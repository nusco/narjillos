package org.nusco.narjillos.genomics;

/**
 * A triplet of DNA nucleotides.
 */
public class Codon {

	public static final int SIZE = 3;

	public static final int HASH_SIZE = SIZE * 8;

	private final int[] genes = new int[SIZE];

	public Codon(int... genes) {
		if (genes.length != SIZE)
			throw new RuntimeException("A codon must contain " + SIZE + " genes");
		System.arraycopy(genes, 0, this.genes, 0, genes.length);
	}

	public int getGene(int pos) {
		return genes[pos];
	}

	@Override
	public int hashCode() {
		return genes[0] * 65536 + genes[1] * 256 + genes[2];
	}

	@Override
	public boolean equals(Object obj) {
		Codon other = (Codon) obj;
		for (int i = 0; i < SIZE; i++)
			if (genes[i] != other.genes[i])
				return false;
		return true;
	}

	@Override
	public String toString() {
		String binaryString = "000000000000000000000000" + Integer.toBinaryString(hashCode());
		String trimmedBinaryString = binaryString.substring(binaryString.length() - HASH_SIZE);
		return "Codon:" + trimmedBinaryString;
	}
}
