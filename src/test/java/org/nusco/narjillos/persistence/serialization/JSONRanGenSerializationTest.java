package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;

public class JSONRanGenSerializationTest {

	@Test
	public void serializesAndDeserializesRanGens() {
		NumGen numGen = new NumGen(42);
		for (int i = 0; i < 1000; i++) {
			numGen.nextDouble();
			numGen.nextSerial();
		}

		String json = JSON.toJson(numGen, NumGen.class);
		NumGen deserialized = JSON.fromJson(json, NumGen.class);

		assertEquals(numGen.nextInt(), deserialized.nextInt(), 0.0);
		assertEquals(numGen.nextByte(), deserialized.nextByte(), 0.0);
		assertEquals(numGen.nextDouble(), deserialized.nextDouble(), 0.0);
		assertEquals(numGen.nextSerial(), deserialized.nextSerial());
	}
}
