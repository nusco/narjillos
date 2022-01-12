package org.nusco.narjillos.creature.body.pns;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

public class WaveNerveTest {

	private static final double PRECISION = 0.01;

	@Test
	public void generatesASinusWave() {
		final int LENGTH = 1;
		final double LENGTH_AT_45_DEGREES = 0.707;

		var nerve = new WaveNerve(1.0 / 8);
		double beatRatio = 2.0;

		assertThat(nerve.tick(beatRatio)).isEqualTo(0, within(PRECISION));
		assertThat(nerve.tick(beatRatio)).isEqualTo(LENGTH_AT_45_DEGREES, within(PRECISION));
		assertThat(nerve.tick(beatRatio)).isEqualTo(LENGTH, within(PRECISION));

		// faster left semiplane
		assertThat(nerve.tick(beatRatio)).isEqualTo(0, within(PRECISION));
		assertThat(nerve.tick(beatRatio)).isEqualTo(-LENGTH, within(PRECISION));

		// back to right semiplane
		assertThat(nerve.tick(beatRatio)).isEqualTo(-LENGTH_AT_45_DEGREES, within(PRECISION));
		assertThat(nerve.tick(beatRatio)).isEqualTo(0, within(PRECISION));
	}
}
