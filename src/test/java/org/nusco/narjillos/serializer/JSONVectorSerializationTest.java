package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;

public class JSONVectorSerializationTest {

	@Test
	public void serializesAndDeserializesVectors() {
		Vector vector = Vector.polar(-200, Double.MAX_VALUE);
		
		String json = JSON.toJson(vector, Vector.class);
		Vector deserialized = JSON.fromJson(json, Vector.class);

		assertEquals(vector, deserialized);
	}

	@Test
	public void nicelyFormatsVectors() {
		Vector vector = Vector.polar(-200, Double.MAX_VALUE);
		String json = JSON.toJson(vector, Vector.class);

		assertEquals("\"(" + vector.x + ", " + vector.y + ")\"", json);
	}
}
