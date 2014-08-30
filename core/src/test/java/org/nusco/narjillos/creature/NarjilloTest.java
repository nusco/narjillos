package org.nusco.narjillos.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.embryogenesis.Embryo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Placemark;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class NarjilloTest {

	Narjillo narjillo;
	Narjillo biggerNarjillo;
	
	@Before
	public void initializeNarjillos() {
		narjillo = new Narjillo(new Body(new Head(10, 10, new ColorByte(10), 1)), Vector.ZERO, new DNA("{255_255_255_255_255_255}{255_255_255_255_255_255}"));
		
		DNA dna = new DNA("{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}");
		biggerNarjillo = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);
	}
	
	@Test
	public void itsInitialEnergyIsProportionalToItsMass() {
		double energyRatio = narjillo.getEnergy().getValue() / biggerNarjillo.getEnergy().getValue();
		double massRatio = narjillo.getMass() / biggerNarjillo.getMass();
		assertEquals(energyRatio, massRatio, 0.001);
	}
	
	@Test
	public void itLosesEnergyFasterWhileMovingIfItIsBigger() {
		double smallerNarjilloEnergyLoss = getEnergyLossWithMovement(narjillo);
		double biggerNarjilloEnergyLoss = getEnergyLossWithMovement(biggerNarjillo);

		assertTrue(biggerNarjilloEnergyLoss > smallerNarjilloEnergyLoss);
	}

	private double getEnergyLossWithMovement(Narjillo narjillo) {
		double startingEnergy = narjillo.getEnergy().getValue();
		Thing target = new Placemark(Vector.cartesian(1000, 1000));
		narjillo.setTarget(target);
		for (int i = 0; i < 10; i++)
			narjillo.tick();
		return startingEnergy - narjillo.getEnergy().getValue();
	}

	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWhenItDoesntMove() {
		DNA dna = new DNA("{1_1_1_1_1_1");
		Narjillo narjilloThatCannotMove = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);

		for (int i = 0; i < Narjillo.MAX_LIFESPAN + 1; i++)
			narjilloThatCannotMove.tick();

		assertTrue(narjilloThatCannotMove.isDead());
	}
}
