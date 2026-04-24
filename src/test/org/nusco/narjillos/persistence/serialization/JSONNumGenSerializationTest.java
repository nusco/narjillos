package org.nusco.narjillos.persistence.serialization;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.NumGen;

import static org.junit.Assert.assertEquals;

public class JSONNumGenSerializationTest {

	@Test
	public void serializesAndDeserializesNumGens() {
		NumGen numGen = new NumGen(42);
		for (int i = 0; i < 1000; i++) {
			numGen.nextDouble();
			numGen.nextSerial();
		}

		String json = JSON.toJson(numGen, NumGen.class);
		NumGen deserialized = JSON.fromJson(json, NumGen.class);

		assertEquals(numGen.nextByte(), deserialized.nextByte());
		assertEquals(numGen.nextInt(), deserialized.nextInt());
		assertEquals(numGen.nextDouble(), deserialized.nextDouble(), 0.0);
		assertEquals(numGen.nextSerial(), deserialized.nextSerial());
	}
}
