package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nusco.narjillos.core.chemistry.Element.*;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;

public class JSONAtmosphereSerializationTest {

	@Test
	public void serializesAndDeserializesAtmospheres() {
		var atmosphere = new Atmosphere(10, 15);

		for (int i = 0; i < 3; i++)
			atmosphere.convert(OXYGEN, NITROGEN);
		for (int i = 0; i < 2; i++)
			atmosphere.convert(HYDROGEN, NITROGEN);

		String json = JSON.toJson(atmosphere, Atmosphere.class);
		Atmosphere deserialized = JSON.fromJson(json, Atmosphere.class);

		assertThat(deserialized.getAmountOf(OXYGEN)).isEqualTo(atmosphere.getAmountOf(OXYGEN));
		assertThat(deserialized.getAmountOf(HYDROGEN)).isEqualTo(atmosphere.getAmountOf(HYDROGEN));
		assertThat(deserialized.getAmountOf(NITROGEN)).isEqualTo(atmosphere.getAmountOf(NITROGEN));

		assertThat(deserialized.getCatalystLevel()).isEqualTo(atmosphere.getCatalystLevel());
	}
}
