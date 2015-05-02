package org.nusco.narjillos.genomics;

// An implementation of the simhash algorithm to find the distance between
// two strains of DNA
public class SimHash {

	public static int getDistance(DNA dna1, DNA dna2) {
		return getDistance(hash(dna1), hash(dna2));
	}

	private static int getDistance(int[] simHash1, int[] simHash2) {
		int result = 0;
		for (int i = 0; i < Codon.HASH_SIZE; i++)
			if (simHash1[i] != simHash2[i])
				result++;
		return result;
	}
	
	// Similar streams of DNA get similar hashes.
	private static int[] hash(DNA dna) {
		int[] hashBits = new int[Codon.HASH_SIZE];

		Codon[] codons = dna.toCodons();
		for (int i = 0; i < codons.length; i++) {
			int codonHash = codons[i].hashCode();
			for (int j = 0; j < hashBits.length; j++) {
				int shift = codonHash >> j;
				if ((shift & 0x1) == 1)
					hashBits[j]++;
				else
					hashBits[j]--;
			}
		}

		int[] result = new int[Codon.HASH_SIZE];
		for (int i = 0; i < hashBits.length; i++)
			if (hashBits[i] > 0)
				result[i] = 1;
		return result;
	}
}
