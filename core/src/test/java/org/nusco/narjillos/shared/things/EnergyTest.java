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
	public void canBeDepleted() {
		assertFalse(energy.isDepleted());

		energy.tick(-mass);
		
		assertTrue(energy.isDepleted());
	}
	
	@Test
	public void cannotFallBelowZero() {
		energy.tick(-mass);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(-10);
		assertEquals(0, energy.getValue(), 0.001);
	}
	
	@Test
	public void cannotIncreaseAgainAfterBeingDepleted() {
		energy.tick(-mass);
		assertEquals(0, energy.getValue(), 0.001);

		energy.tick(10);
		assertEquals(0, energy.getValue(), 0.001);
	}
	
	@Test
	public void increasesByConsumingThings() {
		energy.consume(nutrient);
		
		double expected = mass * Energy.INITIAL_ENERGY_TO_MASS * Energy.MAX_ENERGY_TO_INITIAL_ENERGY;
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
	public void depletedNaturallyDuringItsLifespan() {
		for (int i = 0; i < lifespan - 1; i++)
			energy.tick(0);

		assertFalse(energy.isDepleted());

		energy.tick(0);
		
		assertTrue(energy.isDepleted());
	}

	private void fillToTheMax() {
		double energyValue;
		do {
			energyValue = energy.getValue();
			energy.consume(nutrient);
		} while (energy.getValue() > energyValue);
	}
}
