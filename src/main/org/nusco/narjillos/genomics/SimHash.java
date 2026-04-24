package org.nusco.narjillos.genomics;

// An implementation of Google's SimHash algorithm to hash DNA.
// Similar DNAs get similar hashes. See:
// http://matpalm.com/resemblance/simhash/
class SimHash {

	public static int[] calculateSimHash(DNA dna) {
		int[] bitDensity = calculateBitDensity(dna.toCodons());

		int[] result = new int[Codon.HASH_SIZE];
		for (int i = 0; i < bitDensity.length; i++)
			if (bitDensity[i] > 0)
				result[i] = 1;
		return result;
	}

	static int[] calculateBitDensity(Codon[] codons) {
		int[] result = new int[Codon.HASH_SIZE];

		for (Codon codon : codons) {
			int codonHash = codon.hashCode();
			for (int j = 0; j < result.length; j++) {
				int bitMask = 0b1 << (Codon.HASH_SIZE - j - 1);
				if ((codonHash & bitMask) == 0)
					result[j]--;
				else
					result[j]++;
			}
		}

		return result;
	}
}
