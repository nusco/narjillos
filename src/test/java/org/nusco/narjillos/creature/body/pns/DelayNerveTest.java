package org.nusco.narjillos.creature.body.pns;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DelayNerveTest {

	@Test
	public void delaysASignal() {
		var nerve = new DelayNerve(3);

		assertThat(nerve.tick(1)).isEqualTo(0);
		assertThat(nerve.tick(2)).isEqualTo(0);
		assertThat(nerve.tick(3)).isEqualTo(0);
		assertThat(nerve.tick(4)).isEqualTo(1);
		assertThat(nerve.tick(5)).isEqualTo(2);
		assertThat(nerve.tick(6)).isEqualTo(3);
	}

	@Test
	public void becomesAPassNerveWhenTheDelayIsZero() {
		var nerve = new DelayNerve(0);

		assertThat(nerve.tick(1)).isEqualTo(1);
		assertThat(nerve.tick(2)).isEqualTo(2);
		assertThat(nerve.tick(3)).isEqualTo(3);
	}
}
