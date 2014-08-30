package org.nusco.narjillos.shared.utilities;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates pseudo-random numbers.
 * 
 * It's meant to be seeded to generate a predictable sequence. All the
 * randomness in the system should come from this class after seeding, to get
 * deterministic results from experiments.
 */
public class RanGen {

	private static TransparentRandom random = null;
	private static Thread authorizedThread = null;

	public synchronized static void initializeWith(long seed) {
		if (random != null)
			throw new RuntimeException("RanGen initialized more than once. " + getExplanation());

		random = new TransparentRandom();
		random.setSeed(seed);

		authorizedThread = Thread.currentThread();
	}

	public static double nextDouble() {
		return getRandom().nextDouble();
	}

	public static int nextInt() {
		return getRandom().nextInt();
	}

	public static int nextByte() {
		return Math.abs(nextInt()) % 255;
	}

	public static synchronized Random getRandom() {
		if (random == null)
			throw new RuntimeException("Initialize RanGen before using it. (Call initializeWith(seed)).");
		if (Thread.currentThread() != authorizedThread)
			throw new RuntimeException("RanGen accessed from multiple threads. " + getExplanation());
		return random;
	}

	static long getSeed() {
		return ((TransparentRandom) getRandom()).getCurrentSeed();
	}

	public static long getCurrentSeed() {
		return random.getCurrentSeed();
	}

	private static String getExplanation() {
		return "(Don't do that, or else there is no guarantee that the same " + "seed will generate the same sequence of numbers.)";
	}

	// Don't use in production code - this is just for testing.
	// It throws an exception, so that you won't use it by mistake.
	public static synchronized void reset() {
		random = null;
		throw new RuntimeException("RanGen.reset() called. " + getExplanation());
	}

	/**
	 * A Random that allows you to get and set the current seed.
	 */
	private static class TransparentRandom extends Random {
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized void setSeed(long seed) {
			// Don't scramble the seed like the superclass does.
			extractSeed().set(seed);
		}

		public long getCurrentSeed() {
			return extractSeed().longValue();
		}

		private AtomicLong extractSeed() {
			// Put on your gloves - this is going to be dirty.
			try {
				Field seedField = Random.class.getDeclaredField("seed");
				seedField.setAccessible(true);
				return (AtomicLong) seedField.get(this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}