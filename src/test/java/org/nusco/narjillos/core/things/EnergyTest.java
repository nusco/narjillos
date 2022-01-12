package org.nusco.narjillos.core.things;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.configuration.Configuration;

public class EnergyTest {

	private static final double PRECISION_0001 = 0.001;

	private final double initialValue = 10;
	private final double lifespan = 100;

	private final Energy energy = new LifeFormEnergy(initialValue, lifespan);
	private final Energy otherEnergy = Energy.INFINITE;

	@Test
	public void itStartsWithTheInitialValue() {
		assertThat(initialValue).isEqualTo(energy.getValue());
	}

	@Test
	public void canBeDepleted() {
		assertThat(energy.isZero()).isFalse();

		energy.tick(-initialValue);

		assertThat(energy.isZero()).isTrue();
	}

	@Test
	public void cannotFallBelowZero() {
		energy.tick(-initialValue);
		assertThat(energy.getValue()).isEqualTo(0, within(PRECISION_0001));

		energy.tick(-10);
		assertThat(energy.getValue()).isEqualTo(0, within(PRECISION_0001));
	}

	@Test
	public void cannotIncreaseAgainAfterBeingDepleted() {
		energy.tick(-initialValue);
		assertThat(energy.getValue()).isEqualTo(0, within(PRECISION_0001));

		energy.tick(10);
		assertThat(energy.getValue()).isEqualTo(0, within(PRECISION_0001));
	}

	@Test
	public void increasesByAbsorbingOtherEnergy() {
		energy.absorb(otherEnergy);

		double expected = initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		assertThat(energy.getValue()).isEqualTo(expected, within(PRECISION_0001));
	}

	@Test
	public void neverRaisesOverAAMax() {
		fillToTheMax();
		double initialEnergy = energy.getValue();

		energy.absorb(otherEnergy);

		assertThat(energy.getValue()).isEqualTo(initialEnergy, within(0.00001));
		assertThat(energy.getValue()).isEqualTo(energy.getMaximumValue(), within(0.00001));
	}

	@Test
	public void itsMaximumValueDecreasesWithAge() {
		fillToTheMax();

		double fullEnergyWhenStillYoung = energy.getValue();

		// get older
		energy.tick(0);

		energy.absorb(otherEnergy);
		double fullEnergyWhenSlightlyOlder = energy.getValue();

		assertThat(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder).isTrue();

		energy.absorb(otherEnergy);

		assertThat(energy.getValue()).isEqualTo(fullEnergyWhenSlightlyOlder, within(PRECISION_0001));
	}

	@Test
	public void depletesNaturallyDuringItsLifespan() {
		fillToTheMax();

		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertThat(energy.isZero()).isFalse();

		energy.tick(0);

		assertThat(energy.isZero()).isTrue();
	}

	@Test
	public void canBeDroppedToZero() {
		energy.dropToZero();

		assertThat(energy.isZero()).isTrue();
	}

	@Test
	public void canBeDamaged() {
		double value = energy.getValue();
		energy.damage();

		assertThat(energy.getValue() < value).isTrue();

		for (int i = 0; i < 100; i++)
			energy.damage();

		assertThat(energy.isZero()).isTrue();
	}

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.absorb(otherEnergy);
		} while (energy.getValue() > energyValue);
	}
}
