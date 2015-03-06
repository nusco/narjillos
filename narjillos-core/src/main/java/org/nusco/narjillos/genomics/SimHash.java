package org.nusco.narjillos.genomics;

import org.nusco.narjillos.creature.Narjillo;

// An implementation of the simhash algorithm. Similar streams of DNA end up
// with similar hashes.
public class SimHash {

	private static final int HASH_SIZE = 30;

	public static int simHash(Narjillo narjillo) {
		int hash = hash(narjillo);
		System.out.println(getDifferences(hash, 0) + " " + narjillo.getVisualHash());
		return getDifferences(hash, Integer.MAX_VALUE);
	}

	public static int getDistance(Narjillo narjillo1, Narjillo narjillo2) {
		return getDifferences(hash(narjillo1), hash(narjillo2));
	}

	private static int getDifferences(int simHash1, int simHash2) {
		int result = 0;
		int xorHash = simHash1 ^ simHash2;
		for (int i = 0; i < HASH_SIZE; i++)
			if (((xorHash >> i) & 1) == 1)
				result++;
		return result;
	}
	
	private static int hash(Narjillo narjillo) {
		int[] hashBits = new int[HASH_SIZE];

		Integer[] words = narjillo.getDNA().getGenes();
		for (int i = 0; i < words.length; i++) {
			int geneHash = words[i].hashCode();
			for (int j = 0; j < HASH_SIZE; j++) {
				if (((geneHash >> j) & 1) == 1)
					hashBits[j]++;
				else
					hashBits[j]--;
			}
		}

		int result = 0;
		for (int i = 0; i < hashBits.length; i++)
			if (hashBits[i] > 0)
				result |= (1 << i);
		return result;
	}

}
