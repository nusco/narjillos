package org.nusco.narjillos.shared.physics;

/**
 * Copied and adapted from http://blog.numfum.com/2007/09/java-fixed-point-maths.html
 * (the original source is at http://www.numfum.com/cw/fixed-260907.zip).
 */
public final class ApproxMath {
	private static final int FIXED_POINT = 16;
	private static final double MULTIPLICATION_FACTOR = Math.pow(2, FIXED_POINT);

	// Discrete steps in 90 degrees. Must be a power of 2.
	private static final int QUARTER_CIRCLE = 64;

	// Mask used to limit angles to one revolution. If a quarter circle is 64
	// (i.e. 90 degrees is broken into 64 steps) then the mask is 255.
	private static final int FULL_CIRCLE_MASK = QUARTER_CIRCLE * 4 - 1;

	// The trig table is generated at a higher precision than the typical 16.16
	// format used for the rest of the fixed point maths. The table values are
	// then shifted to match the actual fixed point used.
	private static final int TABLE_SHIFT = 30;

	// Note: if either QUARTER_CIRCLE or TABLE_SHIFT is changed, the two values
	// below will need recalculating (put the above formula into a calculator,
	// set radians, then shift the result by TABLE_SHIFT).
	
	// Equivalent to: sin((2 * PI) / (QUARTER_CIRCLE * 4))
	private static final long SIN_PRECALC = 26350943;
	// Equivalent to: cos((2 * PI) / (QUARTER_CIRCLE * 4)) * 2
	private static final long COS_PRECALC = 2146836866;

	// One quarter sine wave as fixed point values.
	private static final long[] SINE_TABLE = new long[QUARTER_CIRCLE + 1];

	// Scale value for indexing ATAN_TABLE[].
	private static final int ATAN_SHIFT;

	// Reverse atan lookup table.
	private static final byte[] ATAN_TABLE;
	private static final int ATAN_TABLE_LEN;

	static {
		// Generate the sine table using recursive synthesis.
		SINE_TABLE[0] = 0;
		SINE_TABLE[1] = SIN_PRECALC;
		for (int n = 2; n < QUARTER_CIRCLE + 1; n++) {
			SINE_TABLE[n] = ((SINE_TABLE[n - 1] * COS_PRECALC) >> TABLE_SHIFT) - SINE_TABLE[n - 2];
		}
		// Scale the values to the fixed point format used.
		for (int n = 0; n < QUARTER_CIRCLE + 1; n++) {
			SINE_TABLE[n] = SINE_TABLE[n] + (1 << (TABLE_SHIFT - FIXED_POINT - 1)) >> TABLE_SHIFT - FIXED_POINT;
		}

		// Calculate a shift used to scale atan lookups
		int rotl = 0;
		long tan0 = tan(0);
		long tan1 = tan(1);
		while (rotl < 64) {
			if ((tan1 >>= 1) > (tan0 >>= 1)) {
				rotl++;
			} else {
				break;
			}
		}
		ATAN_SHIFT = rotl;
		// Create the a table of tan values
		int[] lut = new int[QUARTER_CIRCLE];
		for (int n = 0; n < QUARTER_CIRCLE; n++) {
			lut[n] = (int) (tan(n) >> rotl);
		}
		ATAN_TABLE_LEN = (int) lut[QUARTER_CIRCLE - 1];
		// Then from the tan values create a reverse lookup
		ATAN_TABLE = new byte[ATAN_TABLE_LEN];
		for (byte n = 0; n < QUARTER_CIRCLE - 1; n++) {
			int min = lut[n];
			int max = lut[n + 1];
			for (int i = min; i < max; i++) {
				ATAN_TABLE[i] = n;
			}
		}
	}

	public static void setUp() {
		// Just for convenience, to use before a performance test.
		// The real setup is performed by just loading the class -
		// this method is a way to load the class in a way that
		// looks nice.
	}

	public static int intToFixed(int n) {
		return n << FIXED_POINT;
	}

	public static double fixedToDouble(long n) {
		return n / MULTIPLICATION_FACTOR;
	}

	private static long div(long a, long b) {
		return (a << FIXED_POINT * 2) / b >> FIXED_POINT;
	}

	private static long sin(int angle) {
		angle &= FULL_CIRCLE_MASK;
		if (angle < QUARTER_CIRCLE * 2) {
			if (angle < QUARTER_CIRCLE) {
				return SINE_TABLE[angle];
			} else {
				return SINE_TABLE[QUARTER_CIRCLE * 2 - angle];
			}
		} else {
			if (angle < QUARTER_CIRCLE * 3) {
				return -SINE_TABLE[angle - QUARTER_CIRCLE * 2];
			} else {
				return -SINE_TABLE[QUARTER_CIRCLE * 4 - angle];
			}
		}
	}

	private static long cos(int angle) {
		angle &= FULL_CIRCLE_MASK;
		if (angle < QUARTER_CIRCLE * 2) {
			if (angle < QUARTER_CIRCLE) {
				return SINE_TABLE[QUARTER_CIRCLE - angle];
			} else {
				return -SINE_TABLE[angle - QUARTER_CIRCLE];
			}
		} else {
			if (angle < QUARTER_CIRCLE * 3) {
				return -SINE_TABLE[QUARTER_CIRCLE * 3 - angle];
			} else {
				return SINE_TABLE[angle - QUARTER_CIRCLE * 3];
			}
		}
	}

	private static long tan(int angle) {
		return div(sin(angle), cos(angle));
	}

	private static int atan(int angle) {
		angle = angle + (1 << (ATAN_SHIFT - 1)) >> ATAN_SHIFT;
		if (angle < 0) {
			if (angle <= -ATAN_TABLE_LEN) {
				return -(QUARTER_CIRCLE - 1);
			}
			return -ATAN_TABLE[-angle];
		} else {
			if (angle >= ATAN_TABLE_LEN) {
				return QUARTER_CIRCLE - 1;
			}
			return ATAN_TABLE[angle];
		}
	}

	/**
	 * Returns the polar angle of a rectangular coordinate.
	 */
	private static long atan(int x, int y) {
		int n = atan((int) div(x, abs(y) + 1)); // kludge to prevent ArithmeticException
		if (y > 0) {
			return n;
		}
		if (y < 0) {
			if (x < 0) {
				return -QUARTER_CIRCLE * 2 - n;
			}
			if (x > 0) {
				return QUARTER_CIRCLE * 2 - n;
			}
			return QUARTER_CIRCLE * 2;
		}
		if (x > 0) {
			return QUARTER_CIRCLE;
		}
		return -QUARTER_CIRCLE;
	}

	/**
	 * Fixed point square root.
	 * <p>
	 * Derived from a 1993 Usenet algorithm posted by Christophe Meessen.
	 */
	public static int sqrt(int n) {
		if (n <= 0) {
			return 0;
		}
		long sum = 0;
		int bit = 0x40000000;
		while (bit >= 0x100) { // lower values give more accurate results
			long tmp = sum | bit;
			if (n >= tmp) {
				n -= tmp;
				sum = tmp + bit;
			}
			bit >>= 1;
			n <<= 1;
		}
		return (int) (sum >> 16 - (FIXED_POINT / 2));
	}

	private static int abs(int n) {
		return (n < 0) ? -n : n;
	}

	private static int degToInt(double degrees) {
		return (int) (degrees * QUARTER_CIRCLE / 90);
	}
	
	private static double longToDeg(long angle) {
		return angle * 90.0 / QUARTER_CIRCLE;
	}

	public static double sin(double degrees) {
		return fixedToDouble(sin(degToInt(degrees)));
	}

	public static double cos(double degrees) {
		return fixedToDouble(cos(degToInt(degrees)));
	}

	public static double atan(double x, double y) {
		return longToDeg(atan((int) x, (int) y));
	}

	public static void main(String[] args) {
		setUp();
		
		long start1 = System.currentTimeMillis();
		for (double i = 0; i < 10_000_000; i++)
			sin(i);
		System.out.println((double) (System.currentTimeMillis() - start1) / 1000);

		long start2 = System.currentTimeMillis();
		for (double i = 0; i < 10_000_000; i++)
			Math.sin(Math.toRadians(i));
		System.out.println((double) (System.currentTimeMillis() - start2) / 1000);
	}
}