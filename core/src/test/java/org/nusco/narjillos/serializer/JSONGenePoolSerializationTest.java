package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.DNAObserver;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONGenePoolSerializationTest {

	GenePool genePool = new GenePool();

	@After
	public void tearDownDNAObserver() {
		DNA.setObserver(DNAObserver.NULL);
	}
	
	@Test
	public void serializesAndDeserializesGenePools() {
		RanGen ranGen = new RanGen(1234);

		DNA.setObserver(genePool);
		
		DNA parent = DNA.random(ranGen);
		DNA child1 = parent.copy(ranGen);
		DNA child2 = parent.copy(ranGen);
		parent.copy(ranGen);
		
		child2.removeFromPool();
		
		child1.copy(ranGen);
		DNA child3 = child1.copy(ranGen);

		DNA.setObserver(DNAObserver.NULL);
		
		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);
		
		assertArrayEquals(deserialized.getAncestry(child3).toArray(), genePool.getAncestry(child3).toArray());
		assertEquals(deserialized.getMostSuccessfulDNA().toString(), genePool.getMostSuccessfulDNA().toString());
	}
}
