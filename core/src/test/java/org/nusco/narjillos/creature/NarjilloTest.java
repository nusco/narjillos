package org.nusco.narjillos.creature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Body;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.embryology.Embryo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class NarjilloTest {

	Narjillo narjillo = new Narjillo(new Body(new Head(10, 10, new ColorByte(10), 1)), Vector.ZERO, DNA.random());
	
	DNA dna = new DNA("{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}");
	Narjillo biggerNarjillo = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);
	
	class NullNarjilloEventListener implements NarjilloEventListener {
		@Override
		public void moved(Segment movement) {
		}

		@Override
		public void died() {
		}
	}
	
	@Test
	public void sendsEventWhenMoving() {
		final Segment[] moved = new Segment[] { null };
		narjillo.addEventListener(new NullNarjilloEventListener() {

			@Override
			public void moved(Segment movement) {
				moved[0] = movement;
			}
		});

		narjillo.tick();
		
		assertNotNull(moved[0]);
	}
	
	@Test
	public void itsInitialEnergyIsProportionalToItsMass() {
		double energyRatio = narjillo.getEnergy().getValue() / biggerNarjillo.getEnergy().getValue();
		double massRatio = narjillo.getMass() / biggerNarjillo.getMass();
		assertEquals(energyRatio, massRatio, 0.001);
	}

	@Test
	public void sendsEventWhenDying() {
		final boolean[] died = new boolean[] { false };
		narjillo.addEventListener(new NullNarjilloEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		for (int i = 0; i <= Narjillo.MAX_LIFESPAN; i++)
			narjillo.tick();
		
		assertTrue(died[0]);
	}
	
	@Test
	public void itLosesEnergyFasterWhileMovingIfItIsBigger() {
		double smallerNarjilloEnergyLoss = getEnergyLossWithMovement(narjillo);
		double biggerNarjilloEnergyLoss = getEnergyLossWithMovement(biggerNarjillo);

		assertTrue(biggerNarjilloEnergyLoss > smallerNarjilloEnergyLoss);
	}

	private double getEnergyLossWithMovement(Narjillo narjillo) {
		double startingEnergy = narjillo.getEnergy().getValue();
		narjillo.setTarget(Vector.cartesian(-10, 10));
		for (int i = 0; i < 10; i++)
			narjillo.tick();
		return startingEnergy - narjillo.getEnergy().getValue();
	}

	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWhenItDoesntMove() {
		DNA dna = new DNA("{1_1_1_1_1_1");
		Narjillo narjilloThatCannotMove = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);

		for (int i = 0; i < Narjillo.MAX_LIFESPAN; i++)
			narjilloThatCannotMove.tick();

		assertTrue(narjilloThatCannotMove.isDead());
	}
}
