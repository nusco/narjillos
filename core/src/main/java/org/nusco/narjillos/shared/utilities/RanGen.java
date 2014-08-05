package org.nusco.narjillos.shared.utilities;

import java.util.Random;

/**
 * Generates pseudo-random numbers.
 * 
 * It's meant to be seeded to generate a predictable sequence. All the randomness
 * in the system should come from this class after seeding, to get repeatable
 * results from experiments.
 */
public class RanGen {

	static final int NO_SEED = -1;
	
	private static int seed = NO_SEED;
	private static Random random;
	private static Thread authorizedThread;
	
	public synchronized static void seed(int seed) {
		if (getSeed() != NO_SEED)
			throw new RuntimeException("RanGen seeded more than once. " + getExplanation());
		setSeed(seed);
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

	private static synchronized Random getRandom() {
		if (random == null)
			initialize();
		if (Thread.currentThread() != authorizedThread)
			throw new RuntimeException("RanGen accessed from multiple threads. " + getExplanation());
		return random;
	}

	private static void initialize() {
		if (getSeed() == NO_SEED)
			seedRandomly();
		authorizedThread = Thread.currentThread();
		random = new Random(getSeed());
	}

	static synchronized int getSeed() {
		return seed;
	}

	private static void setSeed(int seed) {
		RanGen.seed = seed;
	}

	private static void seedRandomly() {
		setSeed((int)(Math.random() * Integer.MAX_VALUE));
	}

	private static String getExplanation() {
		return "(Don't do that, or else there is no guarantee that the same " + 
				"seed will generate the same sequence of numbers.)";
	}

	// Don't use in production code - this is just for testing.
	// So it always throws an exception, just in case somebody uses it by mistake.
	static synchronized void reset() {
		random = null;
		seed = NO_SEED;
		throw new RuntimeException("RanGen.reset() called. " + getExplanation());
	}
}
