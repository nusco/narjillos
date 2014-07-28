package org.nusco.narjillos.shared.utilities;

import java.util.Random;

public class RanGen {

	static final int NO_SEED = -1;
	
	private static int seed = NO_SEED;
	private static Random random;
	private static Thread authorizedThread;
	
	public synchronized static void seed(int seed) {
		if (getSeed() != NO_SEED)
			throw new RuntimeException("RanGen seeded more than once. " + getWarningMessage());
		RanGen.seed = seed;
		initialize(seed);
	}

	private static synchronized Random getRandom() {
		if (random == null) {
			int randomSeed = (int)(Math.random() * Integer.MAX_VALUE);
			System.out.println("Random seed: " + randomSeed);
			initialize(randomSeed);
		}
		if (Thread.currentThread() != authorizedThread)
			throw new RuntimeException("RanGen accessed from multiple threads. " + getWarningMessage());
		return random;
	}

	private static void initialize(int seed) {
		authorizedThread = Thread.currentThread();
		random = new Random(seed);
	}

	static synchronized int getSeed() {
		return seed;
	}

	private static String getWarningMessage() {
		return "(Don't do that, or else there is no guarantee that the " + 
				 "seed will generate the same sequence of numbers.)";
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
