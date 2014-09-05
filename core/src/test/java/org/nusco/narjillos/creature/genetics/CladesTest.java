package org.nusco.narjillos.creature.genetics;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.utilities.RanGen;

public class CladesTest {
	
	RanGen ranGen = new RanGen(1234);
	Clades clades = new Clades();

	@Before
	public void setUpObserver() {
		DNA.setObserver(clades);
	}
	
	@After
	public void tearDownObserver() {
		DNA.setObserver(DNAObserver.NULL);
	}
	
	@Test
	public void tracksAncestry() {
		DNA parent1 = new DNA("{0}");
		parent1.copy(ranGen);

		DNA parent2 = new DNA("{0}");
		DNA child2_1 = parent2.copy(ranGen);
		parent2.copy(ranGen);

		DNA child2_2_1 = child2_1.copy(ranGen);

		List<DNA> ancestry = clades.getAncestry(child2_2_1);
		
		assertEquals(3, ancestry.size());
		assertEquals(parent2, ancestry.get(0));
		assertEquals(child2_1, ancestry.get(1));
		assertEquals(child2_2_1, ancestry.get(2));
	}
}
