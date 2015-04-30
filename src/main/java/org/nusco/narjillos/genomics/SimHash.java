package org.nusco.narjillos.genomics;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.creature.Narjillo;

// An implementation of the simhash algorithm. Similar streams of DNA end up
// with similar hashes.
public class SimHash {

	private static final int HASH_SIZE = 30;

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

	public static void main(String[] args) {
		DNA dna1 = new DNA(100000, "{016_028_171_203_248_055_000_023_061_107_096}{246_091_059_197_047_114_131_198_045_220_123}{163_233_254_202_238_046_224_175_199_141_221}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");
		DNA dna2 = new DNA(100000, "{040_028_171_203_248_055_000_023_061_107_255}{246_091_059_197_047_114_131_198_045_220_123}{163_233_254_202_238_046_224_175_199_141_221}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");
		DNA dna3 = new DNA(100000, "{246_091_059_197_047_114_131_198_045_220_123}{016_028_171_203_248_055_000_023_061_107_096}{163_233_254_202_238_046_224_175_199_141_221}{090_058_060_010_103_152_070_152_071_210_157}{219_110_073_091_178_091_229_131_120_043_057}{031_048_118_079_087_033_109_108_118_253_130}");
		Narjillo n1 = new Narjillo(dna1, Vector.ZERO, Energy.INFINITE);
		Narjillo n2 = new Narjillo(dna2, Vector.ZERO, Energy.INFINITE);
		Narjillo n3 = new Narjillo(dna3, Vector.ZERO, Energy.INFINITE);
		System.out.println("n1 - n1: " + SimHash.getDistance(n1, n1));
		System.out.println("n1 - n2: " + SimHash.getDistance(n1, n2));
		System.out.println("n1 - n3: " + SimHash.getDistance(n1, n3));		
	}
}
