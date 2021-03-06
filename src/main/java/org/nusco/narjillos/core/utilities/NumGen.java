package org.nusco.narjillos.core.utilities;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates numbers (mostly pseudo-random ones).
 * <p>
 * A bit like java.math.Random, but strictly deterministic. You must give it a
 * seed during construction, and it will spew out the same exact numbers given
 * the same sequence of calls.
 * <p>
 * You cannot call the same instance of this class from multiple threads,
 * because multithreading and deterministic behavior don't mix. If you try,
 * the NumGen will complain loudly.
 */
public class NumGen {

	private final TransparentRanGen random = new TransparentRanGen();

	private long serial = 0;

	private transient Thread authorizedThread;

	public NumGen(long seed) {
		random.setSeed(seed);
		authorizedThread = Thread.currentThread();
	}

	/**
	 * Returns a value between 0.0 (inclusive) and 1.0 (exclusive).
	 */
	public double nextDouble() {
		checkThreadIsAuthorized();
		return random.nextDouble();
	}

	public int nextInt() {
		checkThreadIsAuthorized();
		return random.nextInt();
	}

	public int nextByte() {
		return Math.abs(nextInt()) % 256;
	}

	public long nextSerial() {
		checkThreadIsAuthorized();
		return ++serial;
	}

	public long getSeed() {
		return random.getSeed();
	}

	private synchronized void checkThreadIsAuthorized() {
		// I apologize for this slightly paranoid defensive code. Bugs with
		// non-deterministic random generators are hard to find, so I have to be
		// extra careful here.

		if (authorizedThread == null) {
			// If the RanGen has been deserialized, the authorized thread
			// could still be null. In this case, the first thread that
			// accesses it wins the role.
			authorizedThread = Thread.currentThread();
			return;
		}

		if (Thread.currentThread() != authorizedThread)
			throw new RuntimeException("RanGen accessed from multiple threads. " + getExplanation());
	}

	private static String getExplanation() {
		return "(Don't do that, or else there is no guarantee that the same " + "seed will generate the same sequence of numbers.)";
	}

	// A Random that allows you to get/set the current seed.
	private static class TransparentRanGen extends Random {

		private static final long serialVersionUID = 1L;

		@Override
		public void setSeed(long seed) {
			extractSeed().set(seed);
		}

		long getSeed() {
			return extractSeed().get();
		}

		// TODO: Fix the now-illegal private field access before it gets removed from Java
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