package org.nusco.narjillos.shared.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApproxMathTest {
	
	private final double PRECISION = 0.001;
	
	@Test
	public void preciselyCalculatesSinForMainAngles() {
		assertEquals(0, FastMath.sin(0), PRECISION);
		assertEquals(1, FastMath.sin(90), PRECISION);
		assertEquals(0, FastMath.sin(180), PRECISION);
		assertEquals(-1, FastMath.sin(270), PRECISION);
	}
	
	@Test
	public void calculatesSinForNegativeAngles() {
		assertEquals(-1, FastMath.sin(-90), PRECISION);
		assertEquals(0, FastMath.sin(-180), PRECISION);
	}
	
	@Test
	public void calculatesApproximateSin() {
		double step = 0.0005;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaSin = Math.sin(Math.toRadians(degrees));
			double fastSin = FastMath.sin(degrees);
			assertEquals("Mismatched sin(" + degrees + ")", javaSin, fastSin, PRECISION);
		}
	}
	
	@Test
	public void preciselyCalculatesCosForMainAngles() {
		assertEquals(1, FastMath.cos(0), PRECISION);
		assertEquals(0, FastMath.cos(90), PRECISION);
		assertEquals(-1, FastMath.cos(180), PRECISION);
		assertEquals(0, FastMath.cos(270), PRECISION);
	}
	
	@Test
	public void calculatesCosForNegativeAngles() {
		assertEquals(0, FastMath.cos(-90), PRECISION);
		assertEquals(-1, FastMath.cos(-180), PRECISION);
	}
	
	@Test
	public void calculatesApproximateCos() {
		double step = 0.0005;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaCos = Math.cos(Math.toRadians(degrees));
			double fastCos = FastMath.cos(degrees);
			assertEquals("Mismatched cos(" + degrees + ")", javaCos, fastCos, PRECISION);
		}
	}
	
	@Test
	public void preciselyCalculatesAtanForMainAngles() {
//		assertEquals(1, FixedMath.atan(1, 0), 0.0);
//		assertEquals(1, FixedMath.atan(0, 1), 0.0);
//		assertEquals(1, FixedMath.atan(-1, 0), 0.0);
//		assertEquals(1, FixedMath.atan(0, -1), 0.0);
	}
	
	@Test
	public void calculatesApproximateAtan() {
//		double step = 0.005;
//		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
//			double javaCos = Math.atan2(Math.toRadians(degrees));
//			double fastCos = FixedMath.cos(degrees);
//			assertEquals("Mismatched sin(" + degrees + ")", javaCos, fastCos, PRECISION);
//		}
	}

//	@Test(expected=ArithmeticException.class)
//	public void throwsAnExceptionOnSquareRootOfNegativeNumber() {
//		ApproxMath.sqrt(-1);
//	}
	
	@Test
	public void calculatesApproximateSquareRoot() {
//		double step = 0.003;
//		for (double n = 0; n < 10_000; n += step) {
//			double javaSqrt = Math.sqrt(n);
//			double fastSqrt = ApproxMath.sqrt(n);
//			assertEquals("Mismatched sqrt(" + n + ")", javaSqrt, fastSqrt, PRECISION);
//		}
	}
}
