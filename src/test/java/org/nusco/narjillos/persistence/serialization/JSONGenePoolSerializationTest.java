package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolWithHistory;
import org.nusco.narjillos.genomics.SimpleGenePool;
import org.nusco.narjillos.persistence.VolatileDNALog;
import org.nusco.narjillos.persistence.serialization.JSON;

public class JSONGenePoolSerializationTest {

	@Test
	public void serializesAndDeserializesGenePools() {
		NumGen numGen = new NumGen(1234);
		GenePool genePool = new GenePoolWithHistory(new VolatileDNALog());

		DNA parent = genePool.createRandomDNA(numGen);
		DNA child1 = genePool.mutateDNA(parent, numGen);
		DNA child2 = genePool.mutateDNA(parent, numGen);
		genePool.mutateDNA(parent, numGen);
		genePool.remove(child2);
		
		genePool.mutateDNA(child1, numGen);
		DNA child3 = genePool.mutateDNA(child1, numGen);

		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);

		assertTrue(deserialized instanceof GenePoolWithHistory);
		assertEquals(deserialized.getCurrentSerialId(), genePool.getCurrentSerialId());
		assertArrayEquals(deserialized.getAncestryOf(child3).toArray(), genePool.getAncestryOf(child3).toArray());
		assertEquals(deserialized.getMostSuccessfulDNA().toString(), genePool.getMostSuccessfulDNA().toString());
	}

	@Test
	public void serializesAndDeserializesSimpleGenePools() {
		GenePool genePool = new SimpleGenePool();

		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);

		assertTrue(deserialized instanceof SimpleGenePool);
		assertEquals(deserialized.getCurrentSerialId(), genePool.getCurrentSerialId());
	}
}
