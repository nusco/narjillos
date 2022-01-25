package org.nusco.narjillos.core.geometry;

/**
 * Fast approximated trigonometry functions based on lookup tables.
 * <p>
 * Inspired by the algorithms in
 * http://blog.numfum.com/2007/09/java-fixed-point-maths.html.
 */
public class FastMath {

	// Consider angles at a 1/100th of a degree resolution.
	private static final int ANGLE_RESOLUTION = 100;

	// Only store angles in the first quadrant, from 0 to 90 included.
	// Results in the remaining three quadrants can be calculated from these.
	private static final int ANGLE_TABLES_LENGTH = 90 * ANGLE_RESOLUTION + 1;

	// The sin table matches angles (in degree) to their sin. The same
	// table is also used to calculate cos.
	private static final double[] SIN_TABLE = new double[ANGLE_TABLES_LENGTH];

	// The resolution of the arctangent table must be enough to
	// track changes of 1/100th of a degree. I did some empirical
	// calculations here, by looking at the minimum distance between consecutive
	// values in the tan table (that is compiled in the static initializer
	// below). We need a much higher resolution than ANGLE_RESOLUTION here
	// (possibly always pow(ANGLE_RESOLUTION, 2)?).
	private static final int ATAN_RESOLUTION = 10_000;

	// The atan table matches tan length to angles in degrees.
	private static final double[] ATAN_TABLE;

	// The code will check for this value, so we store it in a field for quick
	// access. It will be initialized in the static initializer below.
	private static final int ATAN_TABLE_LENGTH;

	static final double LOG_MIN = 1;

	public static final double LOG_MAX = 100;

	private static final double LOG_RESOLUTION = 0.01;

	private static final double[] LOG_TABLE;

	static {
		// The tan table matches angles in the first quadrant to their tangent.
		// We calculate it, use it to calculate the ATAN_TABLE, then throw it
		// away.
		final double[] TAN_TABLE = new double[ANGLE_TABLES_LENGTH];

		// Fill in the SIN_TABLE and the TAN_TABLE.
		final double degreesStep = 1.0 / ANGLE_RESOLUTION;
		double degrees = 0;
		for (int i = 0; i < ANGLE_TABLES_LENGTH; i++) {
			double radians = Math.toRadians(degrees);
			SIN_TABLE[i] = Math.sin(radians);
			TAN_TABLE[i] = Math.tan(radians);
			degrees += degreesStep;
		}

		// The very last value in the tan table is overflowing. (It's close to
		// 90 degrees, so close to an infinite tangent). Set it to the highest
		// possible value.
		TAN_TABLE[TAN_TABLE.length - 1] = Double.MAX_VALUE;

		// This is the highest tangent that we need to care about. So we need an
		// ATAN_TABLE that goes from 0 to this. Anything bigger can be
		// approximated to 90 degrees.
		double maxTan = TAN_TABLE[TAN_TABLE.length - 2];

		// Create the ATAN_TABLE as a reverse lookup of the TAN_TABLE. We only
		// consider the values in the first quadrant. We will calculate the
		// remaining three quadrants from there.
		ATAN_TABLE_LENGTH = (int) (maxTan * ATAN_RESOLUTION + 1);
		ATAN_TABLE = new double[ATAN_TABLE_LENGTH];

		// Fill in the ATAN_TABLE from the TAN_TABLE.
		ATAN_TABLE[0] = 0.0;
		int currentIndexInTanTable = 0;
		for (int i = 1; i < ATAN_TABLE.length; i++) {
			// Increment the position in the TAN_TABLE until we find a tan that
			// matches the current angle.
			double currentTan = ((double) i) / ATAN_RESOLUTION;
			while (currentIndexInTanTable < TAN_TABLE.length && TAN_TABLE[currentIndexInTanTable] <= currentTan)
				currentIndexInTanTable++;

			// Store the angle corresponding to the TAN_TABLE index in the
			// ATAN_TABLE.
			double currentAngle = ((double) currentIndexInTanTable) / ANGLE_RESOLUTION;
			ATAN_TABLE[i] = currentAngle;
		}

		LOG_TABLE = new double[(int) ((LOG_MAX - LOG_MIN) / LOG_RESOLUTION) + 1];
		for (int currentIndexInLogTable = 0; currentIndexInLogTable < LOG_TABLE.length; currentIndexInLogTable++)
			LOG_TABLE[currentIndexInLogTable] = Math.log(LOG_MIN + currentIndexInLogTable * LOG_RESOLUTION);
	}

	@SuppressWarnings("EmptyMethod")
	public static void setUp() {
		// just an excuse to load the class and run the static initializer.
	}

	public static double sin(double angle) {
		double normalizedAngle = normalize(angle);

		if (normalizedAngle < 180) {
			if (normalizedAngle < 90)
				return SIN_TABLE[toIndexInSinTable(normalizedAngle)];
			else
				return SIN_TABLE[toIndexInSinTable(180 - normalizedAngle)];
		} else {
			if (normalizedAngle < 270)
				return -SIN_TABLE[toIndexInSinTable(normalizedAngle - 180)];
			else
				return -SIN_TABLE[toIndexInSinTable(360 - normalizedAngle)];
		}
	}

	public static double cos(double angle) {
		double normalizedAngle = normalize(angle);

		if (normalizedAngle < 180) {
			if (normalizedAngle < 90)
				return SIN_TABLE[toIndexInSinTable(90 - normalizedAngle)];
			else
				return -SIN_TABLE[toIndexInSinTable(normalizedAngle - 90)];
		} else {
			if (normalizedAngle < 270)
				return -SIN_TABLE[toIndexInSinTable(270 - normalizedAngle)];
			else
				return SIN_TABLE[toIndexInSinTable(normalizedAngle - 270)];
		}
	}

	private static double atan(double ratio) {
		if (ratio < 0) {
			int index = (int) (-ratio * ATAN_RESOLUTION);
			if (index >= ATAN_TABLE_LENGTH)
				return -90;

			return -ATAN_TABLE[index];
		}

		int index = (int) (ratio * ATAN_RESOLUTION);
		if (index >= ATAN_TABLE_LENGTH)
			return 90;

		return ATAN_TABLE[index];
	}

	public static double atan(double y, double x) {
		double arc;
		if (x == 0)
			arc = atan(y / 0.0000001); // avoid divisions by zero
		else
			arc = atan(y / x);

		if (x > 0)
			return arc; // first or fourth quadrant

		if (x < 0) {
			if (y > 0)
				return 180 + arc; // second quadrant
			if (y < 0)
				return -180 + arc; // third quadrant
			// y == 0
			return 180;
		}

		// x == 0
		if (y > 0)
			return 90;
		return -90;
	}

	private static double normalize(double angle) {
		return (angle < 0) ? 360 + angle % 360 : angle % 360;
	}

	private static int toIndexInSinTable(double degrees) {
		return (int) (degrees * ANGLE_RESOLUTION);
	}

	public static void main(String[] args) {
		// Quick performance test. I'll leave this around - I'll probably need
		// it in the future.
		System.out.println("Starting performance test");

		long mathAtanStart = System.currentTimeMillis();
		for (double x = -6000; x < 6000; x += 0.3)
			for (double y = -100; y < 100; y += 0.3)
				Math.toDegrees(Math.atan2(y, x));
		System.out.println((double) (System.currentTimeMillis() - mathAtanStart) / 1000);

		long fastMathAtanStart = System.currentTimeMillis();
		for (double x = -6000; x < 6000; x += 0.3)
			for (double y = -100; y < 100; y += 0.3)
				atan(y, x);
		System.out.println((double) (System.currentTimeMillis() - fastMathAtanStart) / 1000);
	}

	public static double log(double n) {
		if (n < LOG_MIN || n > LOG_MAX)
			throw new RuntimeException("Number out of range in FastMath.log(): " + n);
		return LOG_TABLE[(int) ((n - LOG_MIN) / LOG_RESOLUTION)];
	}
}
