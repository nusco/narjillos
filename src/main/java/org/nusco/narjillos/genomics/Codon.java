package org.nusco.narjillos.genomics;

/**
 * A triplet of DNA nucleotides.
 */
public class Codon {

	public static final int SIZE = 3;

	private final int[] genes = new int[SIZE];

	public Codon(int... genes) {
		if (genes.length != SIZE)
			throw new RuntimeException("A codon must contain " + SIZE + " genes");
		for (int i = 0; i < genes.length; i++)
			this.genes[i] = genes[i];
	}

	public int getGene(int pos) {
		return genes[pos];
	}

	@Override
	public int hashCode() {
		return genes[0] + genes[1] * 256 + genes[2] * 65536;
	}

	@Override
	public boolean equals(Object obj) {
		Codon other = (Codon) obj;
		for (int i = 0; i < SIZE; i++)
			if (genes[i] != other.genes[i])
				return false;
		return true;
	}
}
