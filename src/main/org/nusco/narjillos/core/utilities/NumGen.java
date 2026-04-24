package org.nusco.narjillos.core.utilities;


import io.github.jrlucier.xoro.Xoroshiro128Plus;

/**
 * Generates serial numbers or a deterministic, seeded sequence of pseudo-random
 * numbers.
 * <p>
 * This generator protects deterministic behavior in a multithreaded environment:
 * you can only call it from a single thread. If you try to call the same NumGen
 * from another thread (presumably by mistake), it will rise an exception.
 */
public class NumGen {

	private final Xoroshiro128Plus rng;

	private long serial = 0;

	private transient Thread authorizedThread;

	public NumGen(long seed) {
		this(seed, seed, 0);
	}

	public NumGen(long seed1, long seed2, long lastSerial) {
		authorizedThread = Thread.currentThread();
		rng = new Xoroshiro128Plus(seed1, seed2);
		this.serial = lastSerial;
	}

	public long getSeed1() {
		return rng.getStateA();
	}

	public long getSeed2() {
		return rng.getStateB();
	}

	public long getSerial() {
		return serial;
	}

	/**
	 * Returns a value between 0.0 (inclusive) and 1.0 (exclusive).
	 */
	public double nextDouble() {
		validateAccess();
		return rng.nextDouble();
	}

	public int nextInt() {
		validateAccess();
		return rng.nextInt();
	}

	public int nextByte() {
		return Math.abs(nextInt()) % 256;
	}

	public long nextSerial() {
		validateAccess();
		serial++;
		return serial;
	}

	private synchronized void validateAccess() {
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
			throw new RuntimeException("NumGen accessed from multiple threads. " + getExplanation());
	}

	private static String getExplanation() {
		return "(Don't do that, or else there is no guarantee that the same " + "seed will generate the same sequence of numbers.)";
	}
}