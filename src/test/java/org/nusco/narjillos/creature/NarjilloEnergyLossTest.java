package org.nusco.narjillos.creature;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.genomics.DNA;

public class NarjilloEnergyLossTest {

	private Narjillo smallerNarjillo;
	private Narjillo biggerNarjillo;

	@BeforeEach
	public void initializeNarjillos() {
		smallerNarjillo = new Narjillo(new DNA(1, "{0_255_10_10_255_255_255_255_0_0_0}{0_255_10_10_255_255_255_255_0_0_0}"), Vector.ZERO,
			90, new LifeFormEnergy(1000, Double.MAX_VALUE));

		var dna = new DNA(2,
			"{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}{0_255_255_255_255_255_255_255_0_0_0}");
		biggerNarjillo = new Narjillo(dna, Vector.ZERO, 90, new LifeFormEnergy(1000, Double.MAX_VALUE));
	}

	@Test
	public void itLosesEnergyFasterWhileMovingIfItIsBigger() {
		double smallerNarjilloEnergyLoss = getEnergyLossWithMovement(smallerNarjillo);
		double biggerNarjilloEnergyLoss = getEnergyLossWithMovement(biggerNarjillo);

		assertThat(biggerNarjilloEnergyLoss > smallerNarjilloEnergyLoss).isTrue();
	}

	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWhenItDoesntMove() {
		var dna = new DNA(1, "{1_1_1_1_1_1_1_1_0_0_0}");
		var narjilloThatCannotMove = new Narjillo(dna, Vector.ZERO, 90, new LifeFormEnergy(100, 100));

		for (int i = 0; i < 101; i++)
			narjilloThatCannotMove.tick();

		assertThat(narjilloThatCannotMove.isDead()).isTrue();
	}

	private double getEnergyLossWithMovement(Narjillo narjillo) {
		double startingEnergy = narjillo.getEnergy().getValue();
		narjillo.setTarget(Vector.cartesian(1000, 1000));

		for (int i = 0; i < 10000; i++)
			narjillo.tick();

		return startingEnergy - narjillo.getEnergy().getValue();
	}
}
