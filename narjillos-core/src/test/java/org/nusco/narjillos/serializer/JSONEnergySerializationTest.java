package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.LifeFormEnergy;

public class JSONEnergySerializationTest {

	@Test
	public void serializesAndDeserializesEnergy() {
		Energy energy = new LifeFormEnergy(10, 20);
		for (int i = 0; i < 10; i++)
			energy.tick(i, 0);
		
		String json = JSON.toJson(energy, LifeFormEnergy.class);
		Energy deserialized = JSON.fromJson(json, LifeFormEnergy.class);

		energy.tick(10, 0);
		deserialized.tick(10, 0);

		assertTrue(energy instanceof LifeFormEnergy);
		assertEquals(energy.getValue(), deserialized.getValue(), 0.0);
		assertEquals(energy.getMaximumValue(), deserialized.getMaximumValue(), 0.0);
	}
}
