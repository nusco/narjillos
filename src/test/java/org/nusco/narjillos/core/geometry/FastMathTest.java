package org.nusco.narjillos.core.geometry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FastMathTest {

	private final double EXPECTED_TRIG_PRECISION = 0.001; // 1/1000th of a point

	private final double EXPECTED_ATAN_PRECISION = 0.01;  // 1/100th of a degree

	private final double EXPECTED_LOG_PRECISION = 0.01;

	@Test
	public void preciselyCalculatesSinForMainAngles() {
		assertEquals(0, FastMath.sin(0), EXPECTED_TRIG_PRECISION);
		assertEquals(1, FastMath.sin(90), EXPECTED_TRIG_PRECISION);
		assertEquals(0, FastMath.sin(180), EXPECTED_TRIG_PRECISION);
		assertEquals(-1, FastMath.sin(270), EXPECTED_TRIG_PRECISION);
	}

	@Test
	public void calculatesSinForNegativeAngles() {
		assertEquals(-1, FastMath.sin(-90), EXPECTED_TRIG_PRECISION);
		assertEquals(0, FastMath.sin(-180), EXPECTED_TRIG_PRECISION);
	}

	@Test
	public void calculatesApproximatedSin() {
		double step = 0.0003;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaSin = Math.sin(Math.toRadians(degrees));
			double fastSin = FastMath.sin(degrees);
			assertEquals("Mismatched sin(" + degrees + ")", javaSin, fastSin, EXPECTED_TRIG_PRECISION);
		}
	}

	@Test
	public void preciselyCalculatesCosForMainAngles() {
		assertEquals(1, FastMath.cos(0), EXPECTED_TRIG_PRECISION);
		assertEquals(0, FastMath.cos(90), EXPECTED_TRIG_PRECISION);
		assertEquals(-1, FastMath.cos(180), EXPECTED_TRIG_PRECISION);
		assertEquals(0, FastMath.cos(270), EXPECTED_TRIG_PRECISION);
	}

	@Test
	public void calculatesCosForNegativeAngles() {
		assertEquals(0, FastMath.cos(-90), EXPECTED_TRIG_PRECISION);
		assertEquals(-1, FastMath.cos(-180), EXPECTED_TRIG_PRECISION);
	}

	@Test
	public void calculatesApproximatedCos() {
		final double step = 0.0003;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaCos = Math.cos(Math.toRadians(degrees));
			double fastCos = FastMath.cos(degrees);
			assertEquals("Mismatched cos(" + degrees + ")", javaCos, fastCos, EXPECTED_TRIG_PRECISION);
		}
	}

	@Test
	public void doesNotThrowsExceptionsIfCalculatingAnArcTangentWithZeros() {
		FastMath.atan(0, 10);
		FastMath.atan(10, 0);
	}

	@Test
	public void preciselyCalculatesAtanForMainAngles() {
		assertEquals(0, FastMath.atan(0, 100), 0.0);
		assertEquals(90, FastMath.atan(100, 0), 0.0);
		assertEquals(180, FastMath.atan(0, -100), 0.0);
		assertEquals(-90, FastMath.atan(-100, 0), 0.0);
	}

	@Test
	public void calculatesApproximatedArcTangentForSegmentsCloseToTheAxes() {
		final double VERY_LARGE = Double.MAX_VALUE;
		final double VERY_SMALL = 1 / Double.MAX_VALUE;

		assertEqualsAtan(VERY_SMALL, VERY_LARGE);
		assertEqualsAtan(VERY_SMALL, -VERY_LARGE);
		assertEqualsAtan(-VERY_SMALL, VERY_LARGE);
		assertEqualsAtan(-VERY_SMALL, -VERY_LARGE);

		assertEqualsAtan(VERY_LARGE, VERY_SMALL);
		assertEqualsAtan(VERY_LARGE, -VERY_SMALL);
		assertEqualsAtan(-VERY_LARGE, VERY_SMALL);
		assertEqualsAtan(-VERY_LARGE, -VERY_SMALL);
	}

	// Very slow test, so keep it disabled by default
	//@Test
	public void calculatesApproximatedArcTangent() {
		for (double y = -6000; y <= 6000; y += 0.7)
			for (double x = -100; x <= 100; x += 0.13)
				assertEqualsAtan(y, x);
	}

	private void assertEqualsAtan(double y, double x) {
		double javaAtan = Math.toDegrees(Math.atan2(y, x));
		double fastAtan = FastMath.atan(y, x);
		assertEquals("Mismatched atan(" + y + ", " + x + ")", javaAtan, fastAtan, EXPECTED_ATAN_PRECISION);
	}

	@Test
	public void calculatesApproximatedLogInARange() {
		final double step = 0.0003;
		for (double log = FastMath.LOG_MIN; log <= FastMath.LOG_MAX; log += step) {
			double javaLog = Math.log(log);
			double fastLog = FastMath.log(log);
			assertEquals("Mismatched log(" + log + ")", javaLog, fastLog, EXPECTED_LOG_PRECISION);
		}
	}
}
