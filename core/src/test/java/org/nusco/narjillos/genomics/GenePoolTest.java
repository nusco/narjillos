package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.shared.utilities.RanGen;

public class GenePoolTest {
	
	RanGen ranGen = new RanGen(1234);
	GenePool genePool = new GenePool();

	@Before
	public void setUpObserver() {
		DNA.setObserver(genePool);
	}
	
	@After
	public void tearDownObserver() {
		DNA.setObserver(DNAObserver.NULL);
	}
	
	@Test
	public void tracksAncestry() {
		DNA parent1 = new DNA("{0}");
		parent1.copyWithMutations(ranGen);

		DNA parent2 = new DNA("{0}");
		DNA child2_1 = parent2.copyWithMutations(ranGen);
		parent2.copyWithMutations(ranGen);

		DNA child2_2_1 = child2_1.copyWithMutations(ranGen);

		List<DNA> ancestry = genePool.getAncestry(child2_2_1);
		
		assertEquals(3, ancestry.size());
		assertEquals(parent2, ancestry.get(0));
		assertEquals(child2_1, ancestry.get(1));
		assertEquals(child2_2_1, ancestry.get(2));
	}
	
	@Test
	public void getsMostSuccessfulDNA() {
		new DNA("111_111_111_222_111_000_000_000_000");
		new DNA("111_111_111_111_111_000_000_000_000");
		new DNA("111_111_111_222_222_000_000_000_000");
		new DNA("111_111_222_111_222_000_000_000_000");
		new DNA("111_222_222_222_222_000_000_000_000");

		DNA mostSuccessful = genePool.getMostSuccessfulDNA();
		
		assertEquals("{111_111_111_111_111_000_000_000_000}", mostSuccessful.toString());
	}
	
	@Test
	public void getsNullAsTheMostSuccesfulInAnEmptyPool() {
		assertNull(genePool.getMostSuccessfulDNA());
	}
	
	@Test
	public void canRemoveDNA() {
		new DNA("111_111_111_222_111_000_000_000_000");
		DNA thisOneWillDie = new DNA("111_111_111_111_111_000_000_000_000");
		new DNA("111_111_111_222_222_000_000_000_000");
		new DNA("111_111_222_111_222_000_000_000_000");
		new DNA("111_222_222_222_222_000_000_000_000");

		thisOneWillDie.destroy();
		DNA mostSuccessful = genePool.getMostSuccessfulDNA();
		
		assertEquals("{111_111_111_222_111_000_000_000_000}", mostSuccessful.toString());
	}
	
	@Test
	public void neverRemovesDNAFromHistory() {
		RanGen ranGen = new RanGen(42);
		for (int i = 0; i < 100; i++) {
			DNA dna = DNA.random(ranGen);
			if (i >= 50)
				dna.destroy();
		}
		
		assertEquals(50, genePool.getCurrentPoolSize());
		assertEquals(100, genePool.getHistoricalPoolSize());
	}
}
