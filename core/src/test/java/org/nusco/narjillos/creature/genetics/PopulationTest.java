package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class PopulationTest {
	
	Population population = new Population();
	
	@Before
	public void setupPopulation() {
		population.add(createNarjillo("111-111-111-222"));
		population.add(createNarjillo("111-111-111-111"));
		population.add(createNarjillo("111-111-222-111"));
		population.add(createNarjillo("111-222-222-222"));
	}
	
	@Test
	public void getsMostTypicalSpecimen() {
		Narjillo narjillo = population.getMostTypicalSpecimen();
		
		assertEquals("111-111-111-111", narjillo.getDNA().toString());
	}

	private Narjillo createNarjillo(String genes) {
		return new Narjillo(new Head(0, 0, new ColorByte(0), 1), new DNA(genes));
	}
}
