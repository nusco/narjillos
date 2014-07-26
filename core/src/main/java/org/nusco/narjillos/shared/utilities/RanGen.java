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
	}

	private static synchronized Random getRandom() {
		if (random == null)
			initialize();
		if (Thread.currentThread() != authorizedThread)
			throw new RuntimeException("RanGen accessed from multiple threads. " + getWarningMessage());
		return random;
	}

	private static void initialize() {
		authorizedThread = Thread.currentThread();
		int seed = calculateSeed();
		System.out.println("Random seed: " + seed);
		random = new Random(seed);
	}

	private static int calculateSeed() {
		if (getSeed() == NO_SEED)
			return (int)(Math.random() * Integer.MAX_VALUE);
		return getSeed();
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
