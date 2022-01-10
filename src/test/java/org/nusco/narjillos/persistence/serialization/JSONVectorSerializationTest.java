package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;

public class JSONVectorSerializationTest {

	@Test
	public void serializesAndDeserializesVectors() {
		var vector = Vector.polar(-200, Double.MAX_VALUE);

		String json = JSON.toJson(vector, Vector.class);
		Vector deserialized = JSON.fromJson(json, Vector.class);

		assertThat(deserialized).isEqualTo(vector);
	}

	@Test
	public void nicelyFormatsVectors() {
		Vector vector = Vector.polar(-200, Double.MAX_VALUE);
		String json = JSON.toJson(vector, Vector.class);

		assertThat(json).isEqualTo("\"(" + vector.x + ", " + vector.y + ")\"");
	}
}
