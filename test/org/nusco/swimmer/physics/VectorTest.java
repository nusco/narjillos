package org.nusco.swimmer.physics;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest {
	@Test
	public void hasALocation() {
		Vector p = new Vector(10, 20);
		assertEquals(10, p.getX(), 0);
		assertEquals(20, p.getY(), 0);
	}

	@Test
	public void canBeAddedToAVector() {
		Vector start = new Vector(10, 20);
		Vector end = start.plus(10, 0);
		assertEquals(new Vector(20, 20), end);
	}
}
