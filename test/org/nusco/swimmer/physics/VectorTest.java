package org.nusco.swimmer.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest {
	@Test
	public void hasCoordinates() {
		Vector v = new Vector(10.1, 20.1);
		
		assertEquals(10.1, v.getX(), 0);
		assertEquals(20.1, v.getY(), 0);
	}
	
	@Test
	public void hasAnAngle() {
		assertEquals(0, new Vector(0, 0).getAngle(), 0);
		assertEquals(0, new Vector(0, 1).getAngle(), 0);
		assertEquals(45, new Vector(1, 1).getAngle(), 0);
		assertEquals(90, new Vector(1, 0).getAngle(), 0);
		assertEquals(135, new Vector(1, -1).getAngle(), 0);
		assertEquals(180, new Vector(0, -1).getAngle(), 0);
		assertEquals(-135, new Vector(-1, -1).getAngle(), 0);
		assertEquals(-90, new Vector(-1, 0).getAngle(), 0);
		assertEquals(-45, new Vector(-1, 1).getAngle(), 0);
	}
	
	@Test
	public void hasALength() {
		assertEquals(0, new Vector(0, 0).getLength(), 0);
		assertEquals(1, new Vector(1, 0).getLength(), 0);
		assertEquals(1, new Vector(0, -1).getLength(), 0);
		assertEquals(5, new Vector(3, 4).getLength(), 0.001);
	}

	@Test
	public void canBeAddedToAnotherVector() {
		Vector start = new Vector(10, 20);
	
		Vector end = start.plus(10, 0);
		
		assertEquals(new Vector(20, 20), end);
	}

	@Test
	public void canBeMultipliedByAScalar() {
		Vector original = new Vector(10, -20);
		
		Vector calculated = original.by(-0.5);

		assertEquals(new Vector(-5, 10), calculated);
	}
}
