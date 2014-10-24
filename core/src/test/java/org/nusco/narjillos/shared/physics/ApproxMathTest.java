package org.nusco.narjillos.shared.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ApproxMathTest {
	
	private final double PRECISION = 0.03;
	
	@Test
	public void preciselyCalculatesSinForMainAngles() {
		assertEquals(0, ApproxMath.sin(0), 0.0);
		assertEquals(1, ApproxMath.sin(90), 0.0);
		assertEquals(0, ApproxMath.sin(180), 0.0);
		assertEquals(-1, ApproxMath.sin(270), 0.0);
	}
	
	@Test
	public void calculatesSinForNegativeAngles() {
		assertEquals(-1, ApproxMath.sin(-90), 0.0);
		assertEquals(0, ApproxMath.sin(-180), 0.0);
	}
	
	@Test
	public void calculatesApproximateSin() {
		double step = 0.005;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaSin = Math.sin(Math.toRadians(degrees));
			double fastSin = ApproxMath.sin(degrees);
			assertEquals("Mismatched sin(" + degrees + ")", javaSin, fastSin, PRECISION);
		}
	}
	
	@Test
	public void preciselyCalculatesCosForMainAngles() {
		assertEquals(1, ApproxMath.cos(0), 0.0);
		assertEquals(0, ApproxMath.cos(90), 0.0);
		assertEquals(-1, ApproxMath.cos(180), 0.0);
		assertEquals(0, ApproxMath.cos(270), 0.0);
	}
	
	@Test
	public void calculatesCosForNegativeAngles() {
		assertEquals(0, ApproxMath.cos(-90), 0.0);
		assertEquals(-1, ApproxMath.cos(-180), 0.0);
	}
	
	@Test
	public void calculatesApproximateCos() {
		double step = 0.005;
		for (double degrees = -360; degrees < 360 * 2; degrees += step) {
			double javaCos = Math.cos(Math.toRadians(degrees));
			double fastCos = ApproxMath.cos(degrees);
			assertEquals("Mismatched sin(" + degrees + ")", javaCos, fastCos, PRECISION);
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
}
