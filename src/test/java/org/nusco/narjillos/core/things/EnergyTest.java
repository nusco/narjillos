package org.nusco.narjillos.core.things;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.Configuration;

public class EnergyTest {

	private final double initialValue = 10;

	private final double lifespan = 100;

	private Energy energy = new LifeFormEnergy(initialValue, lifespan);

	private Energy biggerMassEnergy = new LifeFormEnergy(initialValue * 2, lifespan);

	private Energy otherEnergy = Energy.INFINITE;

	@Test
	public void itStartsWithTheInitialValue() {
		assertEquals(energy.getValue(), initialValue, 0.0);
	}

	@Test
	public void canBeDepleted() {
		assertFalse(energy.isZero());

		energy.tick(-initialValue);

		assertTrue(energy.isZero());
	}

	@Test
	public void cannotFallBelowZero() {
		energy.tick(-initialValue);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(-10);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void cannotIncreaseAgainAfterBeingDepleted() {
		energy.tick(-initialValue);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(10);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void increasesByAbsorbingOtherEnergy() {
		energy.absorb(otherEnergy);

		double expected = initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		assertEquals(expected, energy.getValue(), 0.001);
	}

	@Test
	public void neverRaisesOverAAMax() {
		fillToTheMax();
		double initialEnergy = energy.getValue();

		energy.absorb(otherEnergy);

		assertEquals(initialEnergy, energy.getValue(), 0.00001);
		assertEquals(energy.getMaximumValue(), energy.getValue(), 0.00001);
	}

	@Test
	public void itsMaximumValueDecreasesWithAge() {
		fillToTheMax();

		double fullEnergyWhenStillYoung = energy.getValue();

		// get older
		energy.tick(0);

		energy.absorb(otherEnergy);
		double fullEnergyWhenSlightlyOlder = energy.getValue();

		assertTrue(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder);

		energy.absorb(otherEnergy);

		assertEquals(fullEnergyWhenSlightlyOlder, energy.getValue(), 0.001);
	}

	@Test
	public void depletesNaturallyDuringItsLifespan() {
		fillToTheMax();

		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertFalse(energy.isZero());

		energy.tick(0);

		assertTrue(energy.isZero());
	}

	@Test
	public void canBeDroppedToZero() {
		energy.dropToZero();

		assertTrue(energy.isZero());
	}

	@Test
	public void canBeDamaged() {
		double value = energy.getValue();
		energy.damage();

		assertTrue(energy.getValue() < value);

		for (int i = 0; i < 100; i++)
			energy.damage();

		assertTrue(energy.isZero());
	}

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.absorb(otherEnergy);
		} while (energy.getValue() > energyValue);
	}
}
