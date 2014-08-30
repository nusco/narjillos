package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;

public class PopulationTest {
	
	Population population = new Population();
	
	@Before
	public void setupPopulation() {
		population.add(createCreature("111_111_111_222_111_000_000"));
		population.add(createCreature("111_111_111_111_111_000_000"));
		population.add(createCreature("111_111_111_222_222_000_000"));
		population.add(createCreature("111_111_222_111_222_000_000"));
		population.add(createCreature("111_222_222_222_222_000_000"));
	}
	
	@Test
	public void getsMostTypicalSpecimen() {
		Creature creature = population.getMostTypicalSpecimen();
		
		assertEquals("{111_111_111_111_111_000_000}", creature.getDNA().toString());
	}

	private Creature createCreature(final String genes) {
		return new Creature() {
			@Override
			public Segment tick() {
				return null;
			}

			@Override
			public DNA getDNA() {
				return new DNA(genes);
			}

			@Override
			public Vector getPosition() {
				return null;
			}

			@Override
			public String getLabel() {
				return null;
			}

			@Override
			public Energy getEnergy() {
				return null;
			}

			@Override
			public DNA reproduce() {
				return null;
			}

			@Override
			public void feedOn(Thing thing) {
			}

			@Override
			public boolean isDead() {
				return false;
			}
		};
	}
}
