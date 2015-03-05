package org.nusco.narjillos.creature;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.LifeFormEnergy;

public class NarjilloEnergyLossTest {

	Narjillo smallerNarjillo;
	Narjillo biggerNarjillo;
	
	@Before
	public void initializeNarjillos() {
		smallerNarjillo = new Narjillo(new DNA(1, "{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}"), new Body(new Head(10, 10, 10, 10, 10, 1, 0.5)), Vector.ZERO, new LifeFormEnergy(1000, 1000));
		
		DNA dna = new DNA(2, "{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}");
		biggerNarjillo = new Narjillo(dna, new Embryo(dna).develop(), Vector.ZERO, Energy.INFINITE);
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
		Narjillo narjilloThatCannotMove = new Narjillo(dna, new Embryo(dna).develop(), Vector.ZERO, new LifeFormEnergy(100, 100));

		for (int i = 0; i < 101; i++)
			narjilloThatCannotMove.tick();

		assertTrue(narjilloThatCannotMove.isDead());
	}

	private double getEnergyLossWithMovement(Narjillo narjillo) {
		double startingEnergy = narjillo.getEnergy().getValue();
		narjillo.setTarget(Vector.cartesian(1000, 1000));
		for (int i = 0; i < 10; i++)
			narjillo.tick();
		return startingEnergy - narjillo.getEnergy().getValue();
	}
}
