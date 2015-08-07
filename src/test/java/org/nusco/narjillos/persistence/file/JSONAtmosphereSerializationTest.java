package org.nusco.narjillos.persistence.file;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.persistence.file.JSON;

public class JSONAtmosphereSerializationTest {

	@Test
	public void serializesAndDeserializesAtmospheres() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		for (int i = 0; i < 3; i++)
			atmosphere.convert(OXYGEN, NITROGEN);
		for (int i = 0; i < 2; i++)
			atmosphere.convert(HYDROGEN, NITROGEN);
		
		String json = JSON.toJson(atmosphere, Atmosphere.class);
		Atmosphere deserialized = (Atmosphere) JSON.fromJson(json, Atmosphere.class);

		assertEquals(atmosphere.getAmountOf(OXYGEN), deserialized.getAmountOf(OXYGEN));
		assertEquals(atmosphere.getAmountOf(HYDROGEN), deserialized.getAmountOf(HYDROGEN));
		assertEquals(atmosphere.getAmountOf(NITROGEN), deserialized.getAmountOf(NITROGEN));
	}
}
