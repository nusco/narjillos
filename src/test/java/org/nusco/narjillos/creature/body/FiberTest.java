package org.nusco.narjillos.creature.body;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FiberTest {
	
	@Test
	public void hasProportionalComponents_1() {
		Fiber fiber = new Fiber(100, 100, 100);
		
		assertEquals(0.33, fiber.getPercentOfRed(), 0.01);
		assertEquals(0.33, fiber.getPercentOfGreen(), 0.01);
		assertEquals(0.33, fiber.getPercentOfBlue(), 0.01);
	}
	
	@Test
	public void hasProportionalComponents_2() {
		Fiber fiber = new Fiber(100, 100, 0);
		
		assertEquals(0.5, fiber.getPercentOfRed(), 0.01);
		assertEquals(0.5, fiber.getPercentOfGreen(), 0.01);
		assertEquals(0.0, fiber.getPercentOfBlue(), 0.01);
	}
	
	@Test
	public void hasProportionalComponents_3() {
		Fiber fiber = new Fiber(100, 0, 0);
		
		assertEquals(1.0, fiber.getPercentOfRed(), 0.01);
		assertEquals(0.0, fiber.getPercentOfGreen(), 0.01);
		assertEquals(0.0, fiber.getPercentOfBlue(), 0.01);
	}
	
	@Test
	public void clipsComponentsBelowZero() {
		Fiber fiber = new Fiber(-100, 100, 100);

		assertEquals(new Fiber(0, 100, 100), fiber);
	}
	
	@Test
	public void clipsComponentsAbove255() {
		Fiber fiber = new Fiber(300, 100, 100);

		assertEquals(new Fiber(255, 100, 100), fiber);
	}

	@Test
	public void canShift() {
		Fiber fiber = new Fiber(100, 200, 10);
		
		Fiber shiftedFiber = fiber.shift(-10, 10, 1);

		assertEquals(new Fiber(90, 210, 11), shiftedFiber);
	}
}
