package org.nusco.narjillos.shared.physics;

/**
 * Fast approximated math functions based on lookup tables.
 */
public class FastMath {

	private static final long DEG_FRACTIONS = 100;
	private static final double[] SIN_TABLE = new double[(int)(90 * DEG_FRACTIONS + 1)];
	private static final double[] ATAN_TABLE = new double[(int)(90 * DEG_FRACTIONS + 1)];

	static {
		final double step = 1.0 / DEG_FRACTIONS;
		double angle = 0;
		for (int i = 0; i < SIN_TABLE.length; i++) {
			double radians = Math.toRadians(angle);
			SIN_TABLE[i] = Math.sin(radians);
			ATAN_TABLE[i] = Math.atan(radians);
			angle += step;
		}
	}

	public static double sin(double angle) {
		double normalizedAngle = normalize(angle);
		
		if (normalizedAngle < 180) {
			if (normalizedAngle < 90) {
				return SIN_TABLE[toIndex(normalizedAngle)];
			} else {
				return SIN_TABLE[toIndex(180 - normalizedAngle)];
			}
		} else {
			if (normalizedAngle < 270) {
				return -SIN_TABLE[toIndex(normalizedAngle - 180)];
			} else {
				return -SIN_TABLE[toIndex(360 - normalizedAngle)];
			}
		}
	}

	public static double cos(double angle) {
		double normalizedAngle = normalize(angle);
		
		if (normalizedAngle < 180) {
			if (normalizedAngle < 90) {
				return SIN_TABLE[toIndex(90 - normalizedAngle)];
			} else {
				return -SIN_TABLE[toIndex(normalizedAngle - 90)];
			}
		} else {
			if (normalizedAngle < 270) {
				return -SIN_TABLE[toIndex(270 - normalizedAngle)];
			} else {
				return SIN_TABLE[toIndex(normalizedAngle - 270)];
			}
		}
	}
	
	private static int atan(double angle) {
//		double normalizedAngle = angle % 360;
//		
//		if (angle < 0) {
//			if (angle <= -ATAN_TABLE_LEN) {
//				return -(90 - 1);
//			}
//			return -ATAN_TABLE[-angle];
//		} else {
//			if (angle >= ATAN_TABLE_LEN) {
//				return 90 - 1;
//			}
//			return ATAN_TABLE[angle];
//		}
		return 0;
	}

	// assumes that both x and y are != 0
	public static long atan(double x, double y) {
		int n = atan(x / y); // kludge to prevent ArithmeticException
		
		if (y > 0)
			return n;

		if (y < 0) {
			if (x < 0) {
				return -180 - n;
			}
			if (x > 0) {
				return 180 - n;
			}
			return 180;
		}

		if (x > 0)
			return 90;

		return -90;
	}

	private static double normalize(double angle) {
		return (angle < 0) ? 360 + angle % 360 : angle % 360;
	}

	private static int toIndex(double degrees) {
		return (int) (degrees * DEG_FRACTIONS);
	}

	public static void setUp() {
		// just an excuse to load the class and run the static initializer
	}
	
	public static void main(String[] args) {
		setUp();
		
		long start1 = System.currentTimeMillis();
		for (int j = 0; j < 100_000; j++)
			for (double i = 0; i <= 360; i++)
				sin(i);
		System.out.println((double) (System.currentTimeMillis() - start1) / 1000);

		long start2 = System.currentTimeMillis();
		for (int j = 0; j < 100_000; j++)
			for (double i = 0; i <= 360; i++)
				Math.sin(Math.toRadians(i));
		System.out.println((double) (System.currentTimeMillis() - start2) / 1000);
	}
}
