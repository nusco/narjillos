package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONRanGenSerializationTest {

	@Test
	public void serializesAndDeserializesRanGens() {
		RanGen ranGen = new RanGen(42);
		for (int i = 0; i < 1000; i++)
			ranGen.nextDouble();
		
		String json = JSON.toJson(ranGen, RanGen.class);
		double expectedNextDouble = ranGen.nextDouble();
		double expectedCurrentSeed = ranGen.getSeed();

		RanGen deserialized = JSON.fromJson(json, RanGen.class);

		assertEquals(expectedNextDouble, deserialized.nextDouble(), 0.0);
		assertEquals(expectedCurrentSeed, deserialized.getSeed(), 0.0);
	}
}
