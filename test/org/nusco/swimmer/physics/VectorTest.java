package org.nusco.swimmer.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest {
	@Test
	public void canBeCreatedFromItCartesianCoordinates() {
		double x = 10.1;
		double y = 20.1;
		Vector v = Vector.cartesian(x, y);
		
		assertEquals(x, v.getX(), 0);
		assertEquals(y, v.getY(), 0);
	}
	
	@Test
	public void canBeCreatedFromItsPolarCoordinates() {
		int degrees = 30;
		int length = 10;
		Vector v = Vector.polar(degrees, length);
		
		assertEquals(8.66, v.getX(), 0.01);
		assertEquals(5.00, v.getY(), 0.01);
	}

	@Test
	public void hasAnAngle() {
		assertEquals(0, Vector.cartesian(0, 1).getAngle(), 0);
		assertEquals(45, Vector.cartesian(1, 1).getAngle(), 0);
		assertEquals(90, Vector.cartesian(1, 0).getAngle(), 0);
		assertEquals(135, Vector.cartesian(1, -1).getAngle(), 0);
		assertEquals(180, Vector.cartesian(0, -1).getAngle(), 0);
		assertEquals(-135, Vector.cartesian(-1, -1).getAngle(), 0);
		assertEquals(-90, Vector.cartesian(-1, 0).getAngle(), 0);
		assertEquals(-45, Vector.cartesian(-1, 1).getAngle(), 0);
	}
	
	@Test
	public void hasALength() {
		assertEquals(0, Vector.cartesian(0, 0).getLength(), 0);
		assertEquals(1, Vector.cartesian(1, 0).getLength(), 0);
		assertEquals(1, Vector.cartesian(0, -1).getLength(), 0);
		assertEquals(5, Vector.cartesian(3, 4).getLength(), 0.001);
	}

	@Test
	public void canBeAddedToAnotherVector() {
		Vector start = Vector.cartesian(10, 20);
	
		Vector end = start.plus(Vector.cartesian(3, -4));
		
		assertEquals(Vector.cartesian(13, 16), end);
	}

	@Test
	public void canBeMultipliedByAScalar() {
		Vector original = Vector.cartesian(10, -20);
		
		Vector calculated = original.by(-0.5);

		assertEquals(Vector.cartesian(-5, 10), calculated);
	}
}
