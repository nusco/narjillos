package org.nusco.narjillos.creature;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.genomics.DNA;

public class NarjilloDamageTest {

	private final Narjillo narjillo = new Narjillo(new DNA(1, "{0_255_10_10_255_255_255_255_0_0_0}{0_255_10_10_255_255_255_255_0_0_0}"), Vector.ZERO, 90,
		new LifeFormEnergy(1000, Double.MAX_VALUE));

	@Test
	public void loosesEnergyWhenDamaged() {
		double initialEnergy = narjillo.getEnergy().getValue();
		narjillo.damage();

		assertThat(narjillo.getEnergy().getValue() < initialEnergy).isTrue();
	}

	@Test
	public void isNormallyNotInPain() {
		assertThat(narjillo.isInPain()).isFalse();
	}

	@Test
	public void isInPainWhenDamaged() {
		narjillo.damage();
		assertThat(narjillo.isInPain()).isTrue();
	}

	@Test
	public void forgetsPainAfterATick() {
		narjillo.damage();
		narjillo.tick();
		assertThat(narjillo.isInPain()).isFalse();
	}
}
