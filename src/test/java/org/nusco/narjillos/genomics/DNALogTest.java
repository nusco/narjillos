package org.nusco.narjillos.genomics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNALog;

public abstract class DNALogTest {

	private DNALog dnaLog;

	public DNALogTest() {
		super();
	}

	@Before
	public void createDababaseHistory() {
		dnaLog = createNewInstance();
	}

	@After
	public void deleteTestDatabase() {
		dnaLog.close();
		dnaLog.delete();
	}

	@Test
	public void savesAndLoadsDna() {
		DNA dna = new DNA(42, "{1_2_3}", 41);
	
		dnaLog.save(dna);
		DNA retrieved = dnaLog.getDna(42);
	
		assertNotNull(retrieved);
		assertEquals(retrieved.getId(), dna.getId());
		assertEquals(retrieved.toString(), dna.toString());
		assertEquals(retrieved.getParentId(), dna.getParentId());
	}

	@Test
	public void returnsNullIfTheDnaIsNotInTheLog() {
		assertNull(dnaLog.getDna(42));
	}

	@Test
	public void silentlySkipsWritingIfADnaIsAlreadyInTheLog() {
		DNA dna = new DNA(42, "{1_2_3}", 41);
	
		dnaLog.save(dna);
		dnaLog.save(dna);
	
		assertEquals(1, dnaLog.getAllDna().size());
	}

	@Test
	public void returnsAllDnaSortedById() {
		dnaLog.save(new DNA(43, "{1_2_3}", 0));
		dnaLog.save(new DNA(42, "{1_2_3}", 42));
	
		List<DNA> genePool = dnaLog.getAllDna();
		
		assertEquals(2, genePool.size());
		assertEquals(dnaLog.getDna(42), genePool.get(0));
		assertEquals(dnaLog.getDna(43), genePool.get(1));
	}

	@Test
	public void returnsLiveDnaSortedById() {
		DNA dna1 = new DNA(42, "{1}", 41);
		dnaLog.save(dna1);
		DNA dna2 = new DNA(43, "{2}", 41);
		dnaLog.save(dna2);
		DNA dna3 = new DNA(41, "{3}", 41);
		dnaLog.save(dna3);
		
		dnaLog.markAsDead(42);
	
		assertEquals(dna3, dnaLog.getLiveDna().get(0));
		assertFalse(dnaLog.getLiveDna().contains(42));
		assertEquals(dna2, dnaLog.getLiveDna().get(1));
	}

	protected abstract DNALog createNewInstance();
}