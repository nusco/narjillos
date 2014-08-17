package org.nusco.narjillos.shared.physics;

/**
 * Fast pre-calculated tables for sin and cos.
 * 
 * On my Mac, they take about 1 extra second at startup (and some memory, of
 * course). After that, they're about 4 times faster than Math.sin() and
 * Math.cos().
 */
public class FastMath {

	private final static double STEP = 0.0001;

	private static double[] sin = new double[(int) (360 / STEP)];
	private static double[] cos = new double[(int) (360 / STEP)];

	static {
		initialize();
	}

	private static void initialize() {
		for (int i = 0; i < sin.length; i++) {
			double angleRadians = Math.toRadians(i * STEP);
			sin[i] = Math.sin(angleRadians);
			cos[i] = Math.cos(angleRadians);
		}
	}

	public static double sin(double angle) {
		return sin[toIndex(angle)];
	}

	public static double cos(double angle) {
		return cos[toIndex(angle)];
	}

	private static double normalize(double angle) {
		double result = angle % 360;
		if (result < 0)
			result = 360 + result;
		if (result >= 360) // 360.0 can still happen because of roundings
			result = 0;
		return result;
	}

	private static int toIndex(double angle) {
		return (int) (normalize(angle) / STEP);
	}
}
