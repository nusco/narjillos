package org.nusco.narjillos.creature.body;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

public class FiberTest {

	private static final double PRECISION = 0.01;

	@Test
	public void hasProportionalComponents_1() {
		var fiber = new Fiber(100, 100, 100);

		assertThat(fiber.getPercentOfRed()).isEqualTo(0.33, within(PRECISION));
		assertThat(fiber.getPercentOfGreen()).isEqualTo(0.33, within(PRECISION));
		assertThat(fiber.getPercentOfBlue()).isEqualTo(0.33, within(PRECISION));
	}

	@Test
	public void hasProportionalComponents_2() {
		var fiber = new Fiber(100, 100, 0);

		assertThat(fiber.getPercentOfRed()).isEqualTo(0.5, within(PRECISION));
		assertThat(fiber.getPercentOfGreen()).isEqualTo(0.5, within(PRECISION));
		assertThat(fiber.getPercentOfBlue()).isEqualTo(0.0, within(PRECISION));
	}

	@Test
	public void hasProportionalComponents_3() {
		var fiber = new Fiber(100, 0, 0);

		assertThat(fiber.getPercentOfRed()).isEqualTo(1.0, within(PRECISION));
		assertThat(fiber.getPercentOfGreen()).isEqualTo(0.0, within(PRECISION));
		assertThat(fiber.getPercentOfBlue()).isEqualTo(0.0, within(PRECISION));
	}

	@Test
	public void clipsComponentsBelowZero() {
		var fiber = new Fiber(-100, 100, 100);

		assertThat(fiber).isEqualTo(new Fiber(0, 100, 100));
	}

	@Test
	public void clipsComponentsAbove255() {
		var fiber = new Fiber(300, 100, 100);

		assertThat(fiber).isEqualTo(new Fiber(255, 100, 100));
	}

	@Test
	public void canShift() {
		var fiber = new Fiber(100, 200, 10);
		var shiftedFiber = fiber.shift(-10, 10, 1);

		assertThat(shiftedFiber).isEqualTo(new Fiber(90, 210, 11));
	}
}
