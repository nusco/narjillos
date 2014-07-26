package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Vector;

public class PopulationTest {
	
	Population population = new Population();
	
	@Before
	public void setupPopulation() {
		population.add(createCreature("111-111-111-222-111")); // genetic distance: 8
		population.add(createCreature("111-111-111-111-111")); // genetic distance: 9
		population.add(createCreature("111-111-111-222-222")); // genetic distance: 7
		population.add(createCreature("111-111-222-111-222")); // genetic distance: 9
		population.add(createCreature("111-222-222-222-222")); // genetic distance: 11
	}
	
	@Test
	public void getsMostTypicalSpecimen() {
		Creature creature = population.getMostTypicalSpecimen();
		
		assertEquals("111-111-111-222-222", creature.getDNA().toString());
	}

	private Creature createCreature(final String genes) {
		return new Creature() {
			@Override
			public void tick() {
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
		};
	}
}
