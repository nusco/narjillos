package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.ecosystem.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.OXYGEN;

import org.junit.Test;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;

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

		assertEquals(atmosphere.getElementLevel(OXYGEN), deserialized.getElementLevel(OXYGEN));
		assertEquals(atmosphere.getElementLevel(HYDROGEN), deserialized.getElementLevel(HYDROGEN));
		assertEquals(atmosphere.getElementLevel(NITROGEN), deserialized.getElementLevel(NITROGEN));
	}
}
