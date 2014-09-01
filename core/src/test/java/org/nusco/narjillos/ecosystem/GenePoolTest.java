package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.embryogenesis.Embryo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.GenePool;
import org.nusco.narjillos.shared.physics.Vector;

public class GenePoolTest {
	
	GenePool population = new GenePool();
	
	@Before
	public void setupPopulation() {
		population.add(createNarjillo("111_111_111_222_111_000_000"));
		population.add(createNarjillo("111_111_111_111_111_000_000"));
		population.add(createNarjillo("111_111_111_222_222_000_000"));
		population.add(createNarjillo("111_111_222_111_222_000_000"));
		population.add(createNarjillo("111_222_222_222_222_000_000"));
	}
	
	@Test
	public void getsMostTypicalSpecimen() {
		Narjillo creature = population.getMostTypicalSpecimen();
		
		assertEquals("{111_111_111_111_111_000_000}", creature.getDNA().toString());
	}

	private Narjillo createNarjillo(String genes) {
		DNA dna = new DNA(genes);
		return new Narjillo(new Embryo(dna).develop(), Vector.ZERO, dna);
	}
}
