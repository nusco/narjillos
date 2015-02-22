package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONGenePoolSerializationTest {

	GenePool genePool = new GenePool();
	
	@Test
	public void serializesAndDeserializesGenePools() {
		RanGen ranGen = new RanGen(1234);

		genePool.enableTracking();
		
		DNA parent = genePool.createRandomDNA(ranGen);
		DNA child1 = genePool.mutateDNA(parent, ranGen);
		DNA child2 = genePool.mutateDNA(parent, ranGen);
		genePool.mutateDNA(parent, ranGen);
		genePool.remove(child2);
		
		genePool.mutateDNA(child1, ranGen);
		DNA child3 = genePool.mutateDNA(child1, ranGen);

		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);

		assertTrue(deserialized.isTracking());
		assertArrayEquals(deserialized.getAncestry(child3).toArray(), genePool.getAncestry(child3).toArray());
		assertEquals(deserialized.getMostSuccessfulDNA().toString(), genePool.getMostSuccessfulDNA().toString());
	}
}
