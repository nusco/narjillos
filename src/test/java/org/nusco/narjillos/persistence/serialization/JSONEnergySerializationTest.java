package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.persistence.serialization.JSON;

public class JSONEnergySerializationTest {

	@Test
	public void serializesAndDeserializesLifeFormEnergy() {
		Energy energy = new LifeFormEnergy(10, 20);
		for (int i = 0; i < -10; i--)
			energy.tick(i);
		
		String json = JSON.toJson(energy, Energy.class);
		Energy deserialized = JSON.fromJson(json, Energy.class);

		assertEquals(energy, deserialized);
	}

	@Test(expected=RuntimeException.class)
	public void throwsExceptionWhenDeserializingInfiniteEnergy() {
		String json = JSON.toJson(Energy.INFINITE, Energy.class);
		JSON.fromJson(json, Energy.class);
	}
}
