package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.NumGen;

public class JSONRanGenSerializationTest {

	@Test
	public void serializesAndDeserializesRanGens() {
		var numGen = new NumGen(42);
		for (int i = 0; i < 1000; i++) {
			numGen.nextDouble();
			numGen.nextSerial();
		}

		String json = JSON.toJson(numGen, NumGen.class);
		NumGen deserialized = JSON.fromJson(json, NumGen.class);

		assertThat(deserialized.nextInt()).isEqualTo(numGen.nextInt());
		assertThat(deserialized.nextByte()).isEqualTo(numGen.nextByte());
		assertThat(deserialized.nextDouble()).isEqualTo(numGen.nextDouble());
		assertThat(deserialized.nextSerial()).isEqualTo(numGen.nextSerial());
	}
}
