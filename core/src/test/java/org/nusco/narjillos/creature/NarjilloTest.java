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

	Narjillo narjillo = new Narjillo(new Body(new Head(10, 10, new ColorByte(10), 1)), DNA.random());

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
		narjillo.setPosition(Vector.ZERO);

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
	public void sendsEventWhenDying() {
		final boolean[] died = new boolean[] { false };
		narjillo.addEventListener(new NullSwimmerEventListener() {

			@Override
			public void died() {
				died[0] = true;
			}
		});

		narjillo.updateEnergyBy(-Narjillo.INITIAL_ENERGY);
		
		assertTrue(died[0]);
	}
	
	@Test
	public void hasLimitedEnergy() {
		narjillo.updateEnergyBy(-Narjillo.INITIAL_ENERGY);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void diesWhenItsEnergyReachesZero() {
		narjillo.updateEnergyBy(-Narjillo.INITIAL_ENERGY);
		
		assertTrue(narjillo.isDead());
	}
	
	@Test
	public void cannotDecreaseItsEnergyAfterDeath() {
		narjillo.updateEnergyBy(-Narjillo.INITIAL_ENERGY);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);

		narjillo.updateEnergyBy(-10);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void cannotIncreaseItsEnergyAfterDeath() {
		narjillo.updateEnergyBy(-Narjillo.INITIAL_ENERGY);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);

		narjillo.updateEnergyBy(10);
		
		assertEquals(0, narjillo.getEnergy(), 0.001);
	}
	
	@Test
	public void itsEnergyNaturallyDecreasesWithOldAgeEvenWithoutMoving() {
		DNA dna = new DNA("{1_1_1_1_1_1");
		Narjillo narjilloThatCannotMove = new Narjillo(new Embryo(dna).develop(), dna);

		while (!narjilloThatCannotMove.isDead())
			narjilloThatCannotMove.tick();
	}
	
	@Test
	public void itsEnergyDecreasesFasterIfItMoves() {
		DNA dna = new DNA("{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}{255_255_255_255_255_255}");
		Narjillo biggerNarjillo = new Narjillo(new Embryo(dna).develop(), dna);

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
		narjillo.updateEnergyBy(Narjillo.INITIAL_ENERGY);
		
		double expected = Narjillo.INITIAL_ENERGY + Narjillo.ENERGY_PER_FOOD_ITEM;
		assertEquals(expected, narjillo.getEnergy(), 0.001);
	}

	@Test
	public void itsEnergyNeverRaisesHigherThanAMax() {
		while (narjillo.getEnergy() < Narjillo.MAX_ENERGY)
			narjillo.feed();
		
		narjillo.feed();
		
		assertEquals(Narjillo.MAX_ENERGY, narjillo.getEnergy(), 0.001);
	}

	@Test
	public void itsEnergyNeverRaisesHigherThanItsMaxEnergyGivenItsAge() {
		while (narjillo.getEnergy() < Narjillo.MAX_ENERGY)
			narjillo.feed();
		
		narjillo.feed();
		double fullEnergyWhenYounger = narjillo.getEnergy();
		
		narjillo.tick();
		narjillo.feed();
		double fullEnergyWhenSlightlyOlder = narjillo.getEnergy();
		
		assertTrue(fullEnergyWhenYounger > fullEnergyWhenSlightlyOlder);

		
		narjillo.feed();
		narjillo.updateEnergyBy(Narjillo.INITIAL_ENERGY);
		
		assertEquals(fullEnergyWhenSlightlyOlder, narjillo.getEnergy(), 0.001);
	}
}
