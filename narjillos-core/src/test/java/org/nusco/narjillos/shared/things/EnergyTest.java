package org.nusco.narjillos.shared.things;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.utilities.Configuration;

public class EnergyTest {

	final double initialValue = 10;
	final double lifespan = 100;
	Energy energy = new Energy(initialValue, lifespan);
	Energy biggerMassEnergy = new Energy(initialValue * 2, lifespan);
	private Thing nutrient = new FoodPiece() {
		@Override
		public Energy getEnergy() {
			return new Energy(1000, Double.MAX_VALUE);
		}
	};

	@Test
	public void itStartsWithTheInitialValue() {
		assertEquals(energy.getValue(), initialValue, 0.0);
	}

	@Test
	public void canBeDepleted() {
		assertFalse(energy.isDepleted());

		energy.tick(initialValue);

		assertTrue(energy.isDepleted());
	}

	@Test
	public void cannotFallBelowZero() {
		energy.tick(initialValue);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(-10);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void cannotIncreaseAgainAfterBeingDepleted() {
		energy.tick(initialValue);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(10);
		assertEquals(0, energy.getValue(), 0.001);
	}

	@Test
	public void increasesByConsumingThings() {
		energy.consume(nutrient);

		double expected = initialValue * Configuration.CREATURE_MAX_ENERGY_TO_INITIAL_ENERGY;
		assertEquals(expected, energy.getValue(), 0.001);
	}

	@Test
	public void neverRaisesHigherThanAMax() {
		fillToTheMax();
		double initialEnergy = energy.getValue();

		energy.consume(nutrient);

		assertEquals(initialEnergy, energy.getValue(), 0.00001);
		assertEquals(energy.getMax(), energy.getValue(), 0.00001);
	}

	@Test
	public void itsMaximumValueDecreasesWithAge() {
		fillToTheMax();

		double fullEnergyWhenStillYoung = energy.getValue();

		// get older
		energy.tick(0);

		energy.consume(nutrient);
		double fullEnergyWhenSlightlyOlder = energy.getValue();

		assertTrue(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder);

		energy.consume(nutrient);

		assertEquals(fullEnergyWhenSlightlyOlder, energy.getValue(), 0.001);
	}

	@Test
	public void depletesNaturallyDuringItsLifespan() {
		fillToTheMax();
		
		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertFalse(energy.isDepleted());

		energy.tick(0);

		assertTrue(energy.isDepleted());
	}

	@Test
	public void canDonateAPercentOfItself() {
		energy.tick(-10);
		double donation = energy.transfer(0.25);

		assertEquals(5, donation, 0.0);
		assertEquals(15, energy.getValue(), 0.0);
	}

	@Test
	public void refusesDonationsIfTheyResultInAValueBelowTheInitialValue() {
		energy.tick(-10);
		double donation = energy.transfer(51);

		assertEquals(0, donation, 0.0);
		assertEquals(20, energy.getValue(), 0.0);
	}

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.consume(nutrient);
		} while (energy.getValue() > energyValue);
	}
}
