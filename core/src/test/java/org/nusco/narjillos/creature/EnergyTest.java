package org.nusco.narjillos.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EnergyTest {

	final double mass = 10;
	final double lifespan = 100;
	Energy energy = new Energy(mass, lifespan);
	Energy biggerMassEnergy = new Energy(mass * 2, lifespan);
			
	@Test
	public void itIsInitiallyProportionalToTheMass() {
		assertEquals(energy.getValue() / biggerMassEnergy.getValue(), 0.5, 0.0);
	}
	
	@Test
	public void diesWhenItReachesZero() {
		assertFalse(energy.isDead());

		energy.tick(-mass);
		
		assertTrue(energy.isDead());
	}
	
	@Test
	public void cannotBeLessThanZero() {
		energy.tick(-mass);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(-10);
		assertEquals(0, energy.getValue(), 0.001);
	}
	
	@Test
	public void cannotIncreaseAfterDeath() {
		energy.tick(-mass);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(10);
		assertEquals(0, energy.getValue(), 0.001);
	}
	
	@Test
	public void increasesByFeeding() {
		energy.increaseByFeeding();
		
		double expected = (mass + mass * Energy.ENERGY_PER_FOOD_ITEM_RATIO) / 10;
		assertEquals(expected, energy.getValue(), 0.001);
	}

	@Test
	public void neverRaisesHigherThanAMax() {
		feedUntilFull();
		double initialEnergy = energy.getValue();
		
		energy.increaseByFeeding();
		
		assertEquals(initialEnergy, energy.getValue(), 0.00001);
	}
	
	@Test
	public void diesNaturallyWhenItsLifespanIsOver() {
		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertFalse(energy.isDead());

		energy.tick(0);
		
		assertTrue(energy.isDead());
	}

	@Test
	public void neverRaisesHigherThanAMaximumConditionedByAge() {
		feedUntilFull();

		double fullEnergyWhenStillYoung = energy.getValue();
		
		// get older
		energy.tick(0);

		energy.increaseByFeeding();
		double fullEnergyWhenSlightlyOlder = energy.getValue();
		
		assertTrue(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder);

		energy.increaseByFeeding();
		
		assertEquals(fullEnergyWhenSlightlyOlder, energy.getValue(), 0.001);
	}

	private void feedUntilFull() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.increaseByFeeding();
		} while (energy.getValue() > energyValue);
	}
}
