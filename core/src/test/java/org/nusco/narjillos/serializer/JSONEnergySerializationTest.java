package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.things.Energy;

public class JSONEnergySerializationTest {

	@Test
	public void serializesAndDeserializesEnergy() {
		Energy energy = new Energy(10, 20);
		for (int i = 0; i < 10; i++)
			energy.tick(i);
		
		String json = JSON.toJson(energy, Energy.class);
		Energy deserialized = JSON.fromJson(json, Energy.class);

		energy.tick(10);
		deserialized.tick(10);

		assertEquals(energy.getValue(), deserialized.getValue(), 0.0);
		assertEquals(energy.getMax(), deserialized.getMax(), 0.0);
		assertEquals(energy.getAgonyLevel(), deserialized.getAgonyLevel(), 0.0);
	}
}
