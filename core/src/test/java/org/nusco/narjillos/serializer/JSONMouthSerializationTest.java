package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.creature.Mouth;
import org.nusco.narjillos.shared.physics.Vector;

public class JSONMouthSerializationTest {

	@Test
	public void serializesAndDeserializesCompasses() {
		Mouth mouth = new Mouth();
		mouth.tick(Vector.ZERO, Vector.polar(179, 1), 15);
		
		String json = JSON.toJson(mouth, Mouth.class);
		Mouth deserialized = JSON.fromJson(json, Mouth.class);

		assertEquals(mouth, deserialized);
	}
}
