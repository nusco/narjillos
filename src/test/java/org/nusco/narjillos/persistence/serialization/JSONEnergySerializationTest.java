package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;

public class JSONEnergySerializationTest {

	@Test
	public void serializesAndDeserializesLifeFormEnergy() {
		Energy energy = new LifeFormEnergy(10, 20);
		for (int i = 0; i < -10; i--)
			energy.tick(i);

		String json = JSON.toJson(energy, Energy.class);
		Energy deserialized = JSON.fromJson(json, Energy.class);

		assertThat(deserialized).isEqualTo(energy);
	}

	@Test
	public void throwsExceptionWhenDeserializingInfiniteEnergy() {
		assertThatThrownBy(() -> {
			String json = JSON.toJson(Energy.INFINITE, Energy.class);
			JSON.fromJson(json, Energy.class);
		}).isInstanceOf(RuntimeException.class);
	}
}
