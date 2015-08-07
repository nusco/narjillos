package org.nusco.narjillos.persistence.file;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.persistence.file.JSON;

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
