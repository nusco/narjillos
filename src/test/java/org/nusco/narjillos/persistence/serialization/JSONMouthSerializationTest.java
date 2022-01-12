package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.creature.body.Mouth;

public class JSONMouthSerializationTest {

	@Test
	public void serializesAndDeserializesCompasses() {
		var mouth = new Mouth();
		mouth.tick(Vector.ZERO, Vector.polar(179, 1), 15);

		String json = JSON.toJson(mouth, Mouth.class);
		Mouth deserialized = JSON.fromJson(json, Mouth.class);

		assertThat(deserialized.getDirection()).isEqualTo(mouth.getDirection());
	}
}
