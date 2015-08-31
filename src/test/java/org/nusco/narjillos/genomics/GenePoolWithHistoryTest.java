package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.persistence.VolatileDNALog;

public class GenePoolWithHistoryTest {
	
	NumGen numGen = new NumGen(1234);
	GenePool genePool = new GenePoolWithHistory(new VolatileDNALog());

	@Test
	public void hasHistory() {
		DNA parent1 = genePool.createDNA("{0}", numGen);
		genePool.mutateDNA(parent1, numGen);

		DNA parent2 = genePool.createDNA("{0}", numGen);
		DNA child2_1 = genePool.mutateDNA(parent2, numGen);
		genePool.mutateDNA(parent2, numGen);

		DNA child2_2_1 = genePool.mutateDNA(child2_1, numGen);

		List<DNA> ancestry = genePool.getAncestryOf(child2_2_1);
		
		assertEquals(3, ancestry.size());
		assertEquals(parent2, ancestry.get(0));
		assertEquals(child2_1, ancestry.get(1));
		assertEquals(child2_2_1, ancestry.get(2));
	}
	
	@Test
	public void getsMostSuccessfulDNA() {
		genePool.createDNA("111_111_111_222_111_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_111_111_111_111_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_111_111_222_222_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_111_222_111_222_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_222_222_222_222_000_000_000_000_000_000_000_000_000", numGen);

		DNA mostSuccessful = genePool.getMostSuccessfulDNA();
		
		assertEquals("{111_111_111_222_111_000_000_000_000_000_000_000_000_000}", mostSuccessful.toString());
	}
	
	@Test
	public void getsNullAsTheMostSuccesfulInAnEmptyPool() {
		assertNull(genePool.getMostSuccessfulDNA());
	}
	
	@Test
	public void removesDNA() {
		genePool.createDNA("111_111_111_222_111_000_000_000_000_000_000_000_000_000", numGen);
		DNA thisOneWillDie = genePool.createDNA("111_111_111_111_111_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_111_111_222_222_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_111_222_111_222_000_000_000_000_000_000_000_000_000", numGen);
		genePool.createDNA("111_222_222_222_222_000_000_000_000_000_000_000_000_000", numGen);

		genePool.remove(thisOneWillDie);
		DNA mostSuccessful = genePool.getMostSuccessfulDNA();
		
		assertEquals("{111_111_111_222_222_000_000_000_000_000_000_000_000_000}", mostSuccessful.toString());
	}
	
	@Test
	public void neverRemovesDNAFromHistory() {
		for (int i = 0; i < 100; i++) {
			DNA dna = genePool.createRandomDNA(numGen);
			if (i >= 50)
				genePool.remove(dna);
		}
		
		assertEquals(50, genePool.getCurrentPool().size());
		assertEquals(100, genePool.getHistoricalPool().size());
	}
	
	@Test
	public void knowsDNAAncestry() {
		DNA gen1 = genePool.createDNA("{0}", numGen);
		DNA gen2 = genePool.mutateDNA(gen1, numGen);
		DNA gen3 = genePool.mutateDNA(gen2, numGen);
		
		List<DNA> ancestry = genePool.getAncestryOf(gen3);
		
		assertEquals(3, ancestry.size());
		assertEquals(gen1, ancestry.get(0));
		assertEquals(gen2, ancestry.get(1));
		assertEquals(gen3, ancestry.get(2));
	}
	
	@Test
	public void knowsDNAGeneration() {
		DNA gen1 = genePool.createDNA("{0}", numGen);
		DNA gen2 = genePool.mutateDNA(gen1, numGen);
		DNA gen3 = genePool.mutateDNA(gen2, numGen);
		
		assertEquals(1, genePool.getGenerationOf(gen1));
		assertEquals(2, genePool.getGenerationOf(gen2));
		assertEquals(3, genePool.getGenerationOf(gen3));
	}
}
