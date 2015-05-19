package org.nusco.narjillos.core.physics;

/**
 * Fast approximated trigonometry functions based on lookup tables.
 * 
 * Inspired by the algorithms in
 * http://blog.numfum.com/2007/09/java-fixed-point-maths.html.
 */
public class FastMath {
	static final double LOG_MIN = 1;
	public static final double LOG_MAX = 100;
	
	public static void setUp() {
		// just an excuse to load the class and run the static initializer.
	}

	public static double sin(double angle) {
		return Math.sin(Math.toRadians(angle));
	}

	public static double cos(double angle) {
		return Math.cos(Math.toRadians(angle));
	}

	public static double atan(double y, double x) {
		return Math.atan2(y, x);
	}

	public static double log(double n) {
		return Math.log(n);
	}
}
