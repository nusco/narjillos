package org.nusco.narjillos.core.utilities;

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
 *
 * The algorithms to generate ints and doubles are shamelessly lifted from
 * java.util.Random.
 */
public class NumGen {

    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long ADDEND = 0xBL;
    private static final long MASK = (1L << 48) - 1;
    private static final double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    private long seed;
    private long serial = 0;

    private transient Thread authorizedThread;

    public NumGen(long seed) {
        this.seed = seed;
        authorizedThread = Thread.currentThread();
    }

    /**
     * Returns a value between 0.0 (inclusive) and 1.0 (exclusive).
     */
    public double nextDouble() {
        checkThreadIsAuthorized();
        return (((long)(next(26)) << 27) + next(27)) * DOUBLE_UNIT;
    }

    public int nextInt() {
        checkThreadIsAuthorized();
        return next(32);
    }

    public int nextByte() {
        return Math.abs(nextInt()) % 256;
    }

    public long nextSerial() {
        checkThreadIsAuthorized();
        return ++serial;
    }

    private int next(int bits) {
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        return (int)(seed >>> (48 - bits));
    }

    private synchronized void checkThreadIsAuthorized() {
        // I apologize for this slightly paranoid defensive code. Bugs with
        // non-deterministic random generators are hard to find, so I have to be
        // extra careful here.

        if (authorizedThread == null) {
            // If the NumGen has been deserialized, the authorized thread
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