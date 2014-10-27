package org.nusco.narjillos.shared.physics;

/**
 * Fast approximated trigonometry functions based on lookup tables.
 * 
 * Inspired by the algorithms in
 * http://blog.numfum.com/2007/09/java-fixed-point-maths.html.
 */
public class FastMath {

	private static final int ANGLE_RESOLUTION = 100; // 1/100th of a degree
	private static final int ANGLE_TABLES_LENGTH = (int) (90 * ANGLE_RESOLUTION + 1);
	private static final double[] SIN_TABLE = new double[ANGLE_TABLES_LENGTH];

	private static final int ATAN_RESOLUTION = 100;
	private static final int ATAN_TABLE_LENGTH;
	private static final double[] ATAN_TABLE;

	static {
		final double[] TAN_TABLE = new double[ANGLE_TABLES_LENGTH];

		final double degreesStep = 1.0 / ANGLE_RESOLUTION;
		double degrees = 0;
		for (int i = 0; i < ANGLE_TABLES_LENGTH; i++) {
			double radians = Math.toRadians(degrees);
			SIN_TABLE[i] = Math.sin(radians);
			TAN_TABLE[i] = Math.tan(radians);
			degrees += degreesStep;
		}

		// the very last value in the tan table is overflowing.
		// set it to the highest possible value
		TAN_TABLE[TAN_TABLE.length - 1] = Double.MAX_VALUE;
		double maxTan = TAN_TABLE[TAN_TABLE.length - 2];

		// create the ATAN_TABLE as a reverse lookup of the TAN_TABLE
		ATAN_TABLE_LENGTH = (int) (maxTan * ATAN_RESOLUTION + 1);
		ATAN_TABLE = new double[ATAN_TABLE_LENGTH];
		int indexInTanTable = 0;
		double tan = 0;
		for (int i = 0; i < ATAN_TABLE.length; i++) {
			ATAN_TABLE[i] = ((double) indexInTanTable) / ANGLE_RESOLUTION;
			tan += 1.0 / ATAN_RESOLUTION;
			while (indexInTanTable < TAN_TABLE.length && TAN_TABLE[indexInTanTable] < tan)
				indexInTanTable++;
		}
	}

	public static void setUp() {
		// just an excuse to load the class and run the static initializer
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
			int index = (int) (-ratio * ANGLE_RESOLUTION);
			if (index >= ATAN_TABLE_LENGTH)
				return -90;

			return -ATAN_TABLE[index];
		}

		int index = (int) (ratio * ANGLE_RESOLUTION);
		if (index >= ATAN_TABLE_LENGTH)
			return 90;

		return ATAN_TABLE[index];
	}

	public static double atan(double x, double y) {
		double arc;
		if (y == 0)
			arc = atan(x / 0.0000001); // avoid divisions by zero
		else
			arc = atan(x / y);

		if (y > 0)
			return arc; // first quadrant

		if (y < 0) {
			if (x > 0)
				return 180 - arc; // second quadrant
			if (x < 0)
				return -180 - arc; // third quadrant
			// x == 0
			return 180;
		}

		// y == 0
		if (x > 0)
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
		// Quick performance test. I'll leave around - I'll probably need it.
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
}
