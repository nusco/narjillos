package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.creature.body.Fiber;

public class JSONFiberSerializationTest {

	@Test
	public void serializesAndDeserializesFibers() {
		var fiber = new Fiber(10, 20, 30);

		String json = JSON.toJson(fiber, Fiber.class);
		Fiber deserialized = JSON.fromJson(json, Fiber.class);

		assertThat(deserialized).isEqualTo(fiber);
	}
}
