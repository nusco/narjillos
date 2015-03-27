package org.nusco.narjillos.shared.things;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.utilities.Configuration;

public class EnergyTest {

	final double initialValue = 10;
	final double lifespan = 100;
	Energy energy = new LifeFormEnergy(initialValue, lifespan);
	Energy biggerMassEnergy = new LifeFormEnergy(initialValue * 2, lifespan);
	private Energy otherEnergy = Energy.INFINITE;

	@Test
	public void itStartsWithTheInitialValue() {
		assertEquals(energy.getValue(), initialValue, 0.0);
	}

	@Test
	public void canBeDepleted() {
		assertFalse(energy.isZero());

		energy.tick(initialValue, 0);

		assertTrue(energy.isZero());
	}

	@Test
	public void cannotFallBelowZero() {
		energy.tick(initialValue, 0);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(-10, 0);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void cannotIncreaseAgainAfterBeingDepleted() {
		energy.tick(initialValue, 0);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(10, 0);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void increasesByConsumingThings() {
		energy.steal(otherEnergy);

		double expected = initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		assertEquals(expected, energy.getValue(), 0.001);
	}

	@Test
	public void neverRaisesHigherThanAMax() {
		fillToTheMax();
		double initialEnergy = energy.getValue();

		energy.steal(otherEnergy);

		assertEquals(initialEnergy, energy.getValue(), 0.00001);
		assertEquals(energy.getMaximumValue(), energy.getValue(), 0.00001);
	}

	@Test
	public void itsMaximumValueDecreasesWithAge() {
		fillToTheMax();

		double fullEnergyWhenStillYoung = energy.getValue();

		// get older
		energy.tick(0, 0);

		energy.steal(otherEnergy);
		double fullEnergyWhenSlightlyOlder = energy.getValue();

		assertTrue(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder);

		energy.steal(otherEnergy);

		assertEquals(fullEnergyWhenSlightlyOlder, energy.getValue(), 0.001);
	}

	@Test
	public void depletesNaturallyDuringItsLifespan() {
		fillToTheMax();
		
		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0, 0);

		assertFalse(energy.isZero());

		energy.tick(0, 0);

		assertTrue(energy.isZero());
	}

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.steal(otherEnergy);
		} while (energy.getValue() > energyValue);
	}
}
