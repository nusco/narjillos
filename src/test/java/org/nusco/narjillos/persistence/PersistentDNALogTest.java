package org.nusco.narjillos.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;

public class PersistentDNALogTest {

	private PersistentDNALog dnaLog;
	
	@Before
	public void createDababaseHistory() {
		dnaLog = new PersistentDNALog("123-TESTING");
	}

	@After
	public void deleteTestDatabase() {
		dnaLog.close();
		dnaLog.delete();
	}
	
	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseFromMultiplePlaces() {
		PersistentDNALog anotherConnectionToTheSameDb = new PersistentDNALog("123-TESTING");
		anotherConnectionToTheSameDb.close();
	}
	

	@Test
	public void savesAndLoadsDNA() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		dnaLog.save(dna);
		DNA retrieved = dnaLog.getDNA(42);

		assertNotNull(retrieved);
		assertEquals(retrieved.getId(), dna.getId());
		assertEquals(retrieved.toString(), dna.toString());
		assertEquals(retrieved.getParentId(), dna.getParentId());
	}
	
	@Test
	public void returnsNullIfTheDNAIsNotInThePool() {
		assertNull(dnaLog.getDNA(42));
	}
	
	@Test
	public void retrievesTheEntireGenePool() {
		dnaLog.save(new DNA(42, "{1_2_3}", 0));
		dnaLog.save(new DNA(43, "{1_2_3}", 42));

		List<DNA> genePool = dnaLog.getAllDNA();
		
		assertEquals(2, genePool.size());
		assertEquals(dnaLog.getDNA(42), genePool.get(0));
		assertEquals(dnaLog.getDNA(43), genePool.get(1));
	}
	
	@Test
	public void silentlySkipsWritingIfADNAIsAlreadyInTheDatabase() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		dnaLog.save(dna);
		dnaLog.save(dna);

		assertEquals(1, dnaLog.getAllDNA().size());
	}
}
