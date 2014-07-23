package org.nusco.narjillos.shared.utilities;

import java.util.Random;

public class RanGen {

	private static Random random;
	
	public static void seed(long seed) {
		random = new Random(seed);
	}

	private static Random getRandom() {
		if (random == null) {
			long seed = (long)(Math.random() * Long.MAX_VALUE);
			System.out.println("Random seed: " + seed);
			random = new Random(seed);
		}
		return random;
	}

	public static double nextDouble() {
		return getRandom().nextDouble();
	}

	public static double nextGaussian() {
		return getRandom().nextGaussian();
	}

	public static int nextInt() {
		return getRandom().nextInt();
	}

	public static int nextByte() {
		return Math.abs(getRandom().nextInt()) % 255;
	}
}
