package org.nusco.narjillos.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;

public class PersistentGenePoolTest {

	private PersistentGenePool db;
	
	@Before
	public void createDababaseHistory() {
		db = new PersistentGenePool("123-TESTING");
	}

	@After
	public void deleteTestDatabase() {
		db.close();
		db.delete();
	}
	
	@Test
	public void doesNotRaiseAnErrorIfConnectingToTheSameDatabaseFromMultiplePlaces() {
		PersistentGenePool anotherConnectionToTheSameDb = new PersistentGenePool("123-TESTING");
		anotherConnectionToTheSameDb.close();
	}
	

	@Test
	public void savesAndLoadsDNA() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		db.save(dna);
		DNA retrieved = db.getDNA(42);

		assertNotNull(retrieved);
		assertEquals(retrieved.getId(), dna.getId());
		assertEquals(retrieved.toString(), dna.toString());
		assertEquals(retrieved.getParentId(), dna.getParentId());
	}
	
	@Test
	public void returnsNullIfTheDNAIsNotInThePool() {
		assertNull(db.getDNA(42));
	}
	
	@Test
	public void retrievesTheEntireGenePool() {
		db.save(new DNA(42, "{1_2_3}", 0));
		db.save(new DNA(43, "{1_2_3}", 42));

		List<DNA> genePool = db.getAllDNA();
		
		assertEquals(2, genePool.size());
		assertEquals(db.getDNA(42), genePool.get(0));
		assertEquals(db.getDNA(43), genePool.get(1));
	}
	
	@Test
	public void silentlySkipsWritingIfADNAIsAlreadyInTheDatabase() {
		DNA dna = new DNA(42, "{1_2_3}", 41);

		db.save(dna);
		db.save(dna);

		assertEquals(1, db.getAllDNA().size());
	}
}
