package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		genePool.enableTracking();
		
		DNA parent = DNA.random(ranGen);
		DNA child1 = parent.copyWithMutations(ranGen);
		DNA child2 = parent.copyWithMutations(ranGen);
		parent.copyWithMutations(ranGen);
		
		child2.destroy();
		
		child1.copyWithMutations(ranGen);
		DNA child3 = child1.copyWithMutations(ranGen);

		DNA.setObserver(DNAObserver.NULL);
		
		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);

		assertTrue(deserialized.isTracking());
		assertArrayEquals(deserialized.getAncestry(child3).toArray(), genePool.getAncestry(child3).toArray());
		assertEquals(deserialized.getMostSuccessfulDNA().toString(), genePool.getMostSuccessfulDNA().toString());
	}
}
