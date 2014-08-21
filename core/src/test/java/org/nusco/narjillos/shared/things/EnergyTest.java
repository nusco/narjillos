package org.nusco.narjillos.shared.things;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;

public class EnergyTest {

	final double mass = 10;
	final double lifespan = 100;
	Energy energy = new Energy(mass, lifespan);
	Energy biggerMassEnergy = new Energy(mass * 2, lifespan);
	private Thing nutrient = new Thing() {

		@Override
		public Vector getPosition() {
			return null;
		}

		@Override
		public void tick() {
		}

		@Override
		public String getLabel() {
			return null;
		}

		@Override
		public Energy getEnergy() {
			return new Energy(1000, Double.MAX_VALUE);
		}};
			
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
	public void increasesByConsumingThings() {
		energy.consume(nutrient);
		
		double expected = (mass + mass * Energy.ENERGY_PER_FOOD_ITEM_RATIO) / 10;
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
	public void diesNaturallyWhenItsLifespanIsOver() {
		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertFalse(energy.isDead());

		energy.tick(0);
		
		assertTrue(energy.isDead());
	}

	@Test
	public void neverRaisesHigherThanAMaximumConditionedByAge() {
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

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.consume(nutrient);
		} while (energy.getValue() > energyValue);
	}
}
