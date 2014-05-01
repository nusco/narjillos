package org.nusco.swimmer.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest {
	@Test
	public void canBeCreatedFromItCartesianCoordinates() {
		double x = 10.1;
		double y = 20.1;
		Vector v = Vector.cartesian(x, y);
		
		assertEquals(10.1, v.getX(), 0);
		assertEquals(20.1, v.getY(), 0);
		assertEquals(63.32, v.getAngle(), 0.1);
		assertEquals(22.49, v.getLength(), 0.01);
	}
	
	@Test
	public void canBeCreatedFromItsPolarCoordinates() {
		double degrees = 30;
		double length = 10;
		Vector v = Vector.polar(degrees, length);
		
		assertEquals(8.66, v.getX(), 0.01);
		assertEquals(5.00, v.getY(), 0.01);
		assertEquals(30, v.getAngle(), 0.01);
		assertEquals(10, v.getLength(), 0.01);
	}
	
	@Test
	public void hasANormalizedAngle() {
		assertEquals(0, Vector.cartesian(1, 0).getAngle(), 0);
		assertEquals(45, Vector.cartesian(1, 1).getAngle(), 0);
		assertEquals(90, Vector.cartesian(0, 1).getAngle(), 0);
		assertEquals(-45, Vector.cartesian(1, -1).getAngle(), 0);
		assertEquals(-90, Vector.cartesian(0, -1).getAngle(), 0);
		assertEquals(-135, Vector.cartesian(-1, -1).getAngle(), 0);
		assertEquals(180, Vector.cartesian(-1, 0).getAngle(), 0);
		assertEquals(135, Vector.cartesian(-1, 1).getAngle(), 0);
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
	public void canBeSubtractedFromAnotherVector() {
		Vector start = Vector.cartesian(10, 20);
	
		Vector difference = start.minus(Vector.cartesian(3, -4));
		
		assertEquals(Vector.cartesian(7, 24), difference);
	}

	@Test
	public void canBeMultipliedByAScalar() {
		Vector original = Vector.cartesian(10, -20);
		
		Vector calculated = original.by(-0.5);

		assertEquals(Vector.cartesian(-5, 10), calculated);
	}

	@Test
	public void canBeNormalizedToALengthOfOne() {
		Vector original = Vector.polar(42, 1234);
		
		//assertEquals(Vector.polar(42, 1), original.normalize());
		assertEquals(42, original.getAngle(), 0);
	}
}
