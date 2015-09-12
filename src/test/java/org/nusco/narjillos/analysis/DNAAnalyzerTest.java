package org.nusco.narjillos.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class DNAAnalyzerTest {

	DNALog dnaLog = new VolatileDNALog();
	DNAAnalyzer dnaAnalyzer = new DNAAnalyzer(dnaLog);

	@Test
	public void getsMostSuccessfulDNA() {
		dnaLog.save(new DNA(1, "111_111_111_222_111_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(2, "111_111_111_111_111_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(3, "111_111_111_222_222_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(4, "111_111_222_111_222_000_000_000_000_000_000_000_000_000"));
		dnaLog.save(new DNA(5, "111_222_222_222_222_000_000_000_000_000_000_000_000_000"));

		DNA mostSuccessful = dnaAnalyzer.getMostSuccessfulDna();
		
		assertEquals(1, mostSuccessful.getId());
	}
	
	@Test
	public void getsNullAsTheMostSuccesfulInAnEmptyPool() {
		assertNull(dnaAnalyzer.getMostSuccessfulDna());
	}

	@Test
	public void getsAGermline() {
		NumGen numGen = new NumGen(123);

		DNA parent1 = new DNA(1,"{0}");
		dnaLog.save(parent1);
		DNA child1_1 = parent1.mutate(2, numGen);
		dnaLog.save(child1_1);
		
		DNA parent2 = new DNA(3, "{0}");
		dnaLog.save(parent2);
		DNA child2_1 = parent2.mutate(4, numGen);
		dnaLog.save(child2_1);
		DNA child2_2 = parent2.mutate(5, numGen);
		dnaLog.save(child2_2);

		DNA child2_2_1 = child2_2.mutate(6, numGen);
		dnaLog.save(child2_2_1);
		
		List<DNA> ancestry = dnaAnalyzer.getGermline(child2_2_1);
		
		assertEquals(3, ancestry.size());
		assertEquals(parent2, ancestry.get(0));
		assertEquals(child2_2, ancestry.get(1));
		assertEquals(child2_2_1, ancestry.get(2));
	}
}
