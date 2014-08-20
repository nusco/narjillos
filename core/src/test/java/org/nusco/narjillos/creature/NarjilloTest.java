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
	double mass = narjillo.getMass();
	
	class NullSwimmerEventListener implements NarjilloEventListener {
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
		narjillo.addEventListener(new NullSwimmerEventListener() {

			@Override
			public void moved(Segment movement) {
				moved[0] = movement;
			}
		});

		narjillo.tick();
		
		assertNotNull(moved[0]);
	}
	
	@Test
	public void hasTheSameEnergyAsOneTenthItsMassWhenItsBorn() {
		assertEquals(mass / 10, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void diesWhenItsEnergyReachesZero() {
		narjillo.updateEnergyBy(-mass);
		
		assertTrue(narjillo.isDead());
	}

	@Test
	public void sendsEventWhenDying() {
		final boolean[] died = new boolean[] { false };
		narjillo.addEventListener(new NullSwimmerEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		narjillo.updateEnergyBy(-narjillo.getMass());
		
		assertTrue(died[0]);
	}
	
	@Test
	public void hasLimitedEnergy() {
		narjillo.updateEnergyBy(-mass);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void cannotDecreaseItsEnergyAfterDeath() {
		narjillo.updateEnergyBy(-mass);
		assertEquals(0, narjillo.getEnergy(), 0.001);

		narjillo.updateEnergyBy(-10);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void cannotIncreaseItsEnergyAfterDeath() {
		narjillo.updateEnergyBy(-mass);
		assertEquals(0, narjillo.getEnergy(), 0.001);

		narjillo.updateEnergyBy(10);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWithoutMoving() {
		DNA dna = new DNA("{1_1_1_1_1_1");
		Narjillo narjilloThatCannotMove = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);

		while (!narjilloThatCannotMove.isDead())
			narjilloThatCannotMove.tick();
	}
	
	//@Test
	public void itsEnergyDecreasesFasterIfItMoves() {
		DNA dna = new DNA("{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}");
		Narjillo biggerNarjillo = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);

		narjillo.setTarget(Vector.cartesian(-10, 10));
		for (int i = 0; i < 10; i++)
			narjillo.tick();

		biggerNarjillo.setTarget(Vector.cartesian(-10, 10));
		for (int i = 0; i < 10; i++)
			biggerNarjillo.tick();

		assertTrue(narjillo.getEnergy() > biggerNarjillo.getEnergy());
	}
	
	@Test
	public void increasesEnergyByFeeding() {
		narjillo.feed();
		narjillo.updateEnergyBy(mass);
		
		double expected = (mass + mass * Narjillo.ENERGY_PER_FOOD_ITEM_RATIO) / 10;
		assertEquals(expected, narjillo.getEnergy(), 0.001);
	}

	@Test
	public void itsEnergyNeverRaisesHigherThanAMax() {
		feedUntilFull();
		double energy = narjillo.getEnergy();
		
		narjillo.feed();
		
		assertEquals(energy, narjillo.getEnergy(), 0.00001);
	}

	@Test
	public void itsEnergyNeverRaisesHigherThanItsMaxEnergyGivenItsAge() {
		feedUntilFull();

		double fullEnergyWhenStillYoung = narjillo.getEnergy();
		
		// get older
		narjillo.tick();

		narjillo.feed();
		double fullEnergyWhenSlightlyOlder = narjillo.getEnergy();
		
		assertTrue(fullEnergyWhenStillYoung > fullEnergyWhenSlightlyOlder);

		narjillo.feed();
		narjillo.updateEnergyBy(narjillo.getMass());
		
		assertEquals(fullEnergyWhenSlightlyOlder, narjillo.getEnergy(), 0.001);
	}

	private void feedUntilFull() {
		double energy = narjillo.getEnergy();
		narjillo.feed();
		
		while (narjillo.getEnergy() > energy) {
			energy = narjillo.getEnergy();
			narjillo.feed();
		}
	}

	@Test
	public void itsEnergyNaturallyDecreasesWithAgeEvenWhenNotMoving() {
		DNA dna = new DNA("{1_1_1_1_1_1}");
		Narjillo narjilloThatCannotMove = new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);

		while (!narjilloThatCannotMove.isDead())
			narjilloThatCannotMove.tick();

		// This test will never terminate unless the Narjillo eventually dies.
		// (Ugly, but easier to read than alternative tests I tried).
	}
}
