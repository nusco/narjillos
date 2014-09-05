package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.genetics.GenePool;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONGenePoolSerializationTest {

	@Test
	public void serializesAndDeserializesClades() {
		RanGen ranGen = new RanGen(1234);
		
		GenePool genePool = new GenePool();
		
		DNA parent = new DNA("{1}");
		DNA child1 = parent.copy(ranGen);
		parent.copy(ranGen);
		
		child1.copy(ranGen);
		DNA child2 = child1.copy(ranGen);
		
		String json = JSON.toJson(genePool, GenePool.class);
		GenePool deserialized = JSON.fromJson(json, GenePool.class);
		
		assertArrayEquals(deserialized.getAncestry(child2).toArray(), genePool.getAncestry(child2).toArray());
		assertEquals(deserialized.getMostSuccessfulDNA(), genePool.getMostSuccessfulDNA());
	}
}
