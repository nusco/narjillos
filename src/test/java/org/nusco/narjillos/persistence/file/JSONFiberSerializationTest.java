package org.nusco.narjillos.persistence.file;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.persistence.file.JSON;

public class JSONFiberSerializationTest {

	@Test
	public void serializesAndDeserializesFibers() {
		Fiber fiber = new Fiber(10, 20, 30);

		String json = JSON.toJson(fiber, Fiber.class);
		Fiber deserialized = (Fiber) JSON.fromJson(json, Fiber.class);

		assertEquals(fiber, deserialized);
	}
}
