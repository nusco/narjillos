package org.nusco.narjillos.creature;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.genomics.DNA;

public class NarjilloEnergyLossTest {

	Narjillo smallerNarjillo;
	Narjillo biggerNarjillo;
	
	@Before
	public void initializeNarjillos() {
		smallerNarjillo = new Narjillo(new DNA(1, "{0_255_10_10_255_255_255_255_0_0_0}{0_255_10_10_255_255_255_255_0_0_0}"), Vector.ZERO, 90, new LifeFormEnergy(1000, Double.MAX_VALUE));
		
		DNA dna = new DNA(2, "{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}");
		biggerNarjillo = new Narjillo(dna, Vector.ZERO, 90, new LifeFormEnergy(1000, Double.MAX_VALUE));
	}
	
	@Test
	public void itLosesEnergyFasterWhileMovingIfItIsBigger() {
		double smallerNarjilloEnergyLoss = getEnergyLossWithMovement(smallerNarjillo);
		double biggerNarjilloEnergyLoss = getEnergyLossWithMovement(biggerNarjillo);

		assertTrue(biggerNarjilloEnergyLoss > smallerNarjilloEnergyLoss);
	}

	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWhenItDoesntMove() {
		DNA dna = new DNA(1, "{1_1_1_1_1_1_1_1_0_0_0}");
		Narjillo narjilloThatCannotMove = new Narjillo(dna, Vector.ZERO, 90, new LifeFormEnergy(100, 100));

		Atmosphere atmosphere = new Atmosphere();
		for (int i = 0; i < 101; i++)
			narjilloThatCannotMove.tick(atmosphere);

		assertTrue(narjilloThatCannotMove.isDead());
	}

	private double getEnergyLossWithMovement(Narjillo narjillo) {
		double startingEnergy = narjillo.getEnergy().getValue();
		narjillo.setTarget(Vector.cartesian(1000, 1000));
		Atmosphere atmosphere = new Atmosphere();
		for (int i = 0; i < 10000; i++)
			narjillo.tick(atmosphere);
		return startingEnergy - narjillo.getEnergy().getValue();
	}
}
