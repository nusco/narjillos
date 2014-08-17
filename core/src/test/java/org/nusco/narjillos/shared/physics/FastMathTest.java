package org.nusco.narjillos.shared.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FastMathTest {

	private static final double PRECISION = 0.00001;
	
	@Test
	public void calculatesSinForAnglesBetweenZeroAnd360() {
		for (double angle = 0; angle <= 360; angle += PRECISION * 10)
			assertEquals(Math.sin(Math.toRadians(angle)), FastMath.sin(angle), PRECISION);
	}

	@Test
	public void calculatesSinForAnglesAbove360() {
		assertEquals(Math.sin(Math.toRadians(0)), FastMath.sin(360), PRECISION);
		assertEquals(Math.sin(Math.toRadians(10)), FastMath.sin(10), PRECISION);
	}

	@Test
	public void calculatesSinForAnglesBelowZero() {
		assertEquals(Math.sin(Math.toRadians(350)), FastMath.sin(-10), PRECISION);
		assertEquals(Math.sin(Math.toRadians(0)), FastMath.sin(-360), PRECISION);
	}

	@Test
	public void calculatesCosForAnglesBetweenZeroAnd360() {
		for (double angle = 0; angle <= 360; angle += PRECISION * 10)
			assertEquals(Math.cos(Math.toRadians(angle)), FastMath.cos(angle), PRECISION);
	}

	@Test
	public void calculatesCosForAnglesAbove360() {
		assertEquals(Math.cos(Math.toRadians(0)), FastMath.cos(360), PRECISION);
		assertEquals(Math.cos(Math.toRadians(10)), FastMath.cos(10), PRECISION);
	}

	@Test
	public void calculatesCosForAnglesBelowZero() {
		assertEquals(Math.cos(Math.toRadians(350)), FastMath.cos(-10), PRECISION);
		assertEquals(Math.cos(Math.toRadians(0)), FastMath.cos(-360), PRECISION);
	}
}
