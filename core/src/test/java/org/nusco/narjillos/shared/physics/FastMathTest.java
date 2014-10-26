package org.nusco.narjillos.shared.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FastMathTest {
	
	private final double EXPECTED_TRIG_PRECISION = 0.001;

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
	
	// Very slow test, so keep it disabled by default
	// TODO: the arctangent should be more precise. investigate how
	//@Test
	public void calculatesApproximatedArcTangent() {
		final double EXPECTED_ATAN_PRECISION = 0.6;

		for (double x = -100; x <= 100; x += 0.13) {
			for (double y = -6000; y <= 6000; y += 0.9) {
				double javaAtan = Math.toDegrees(Math.atan2(x, y));
				double fastAtan = FastMath.atan(x, y);
				assertEquals("Mismatched atan(" + x + ", " + y + ")", javaAtan, fastAtan, EXPECTED_ATAN_PRECISION);
			}
		}
	}
}
