package org.nusco.narjillos.ecosystem.chemistry;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.ecosystem.chemistry.Atmosphere.getInitialElementLevel;
import static org.nusco.narjillos.ecosystem.chemistry.Element.*;

import org.junit.Test;

public class AtmosphereTest {

	@Test
	public void hasElementLevels() {
		Atmosphere atmosphere = new Atmosphere();
		
		assertEquals(getInitialElementLevel(), atmosphere.getElementLevel(OXYGEN));
		assertEquals(getInitialElementLevel(), atmosphere.getElementLevel(HYDROGEN));
		assertEquals(getInitialElementLevel(), atmosphere.getElementLevel(NITROGEN));
	}
	
	@Test
	public void convertsElementsIntoTheirByproduct() {
		Atmosphere atmosphere = new Atmosphere();
		
		for (int i = 0; i < 1000; i++)
			atmosphere.convert(OXYGEN);
		
		assertEquals(getInitialElementLevel() - 1000, atmosphere.getElementLevel(OXYGEN));
		assertEquals(getInitialElementLevel() + 1000, atmosphere.getElementLevel(HYDROGEN));
		assertEquals(getInitialElementLevel(), atmosphere.getElementLevel(NITROGEN));
	}
	
	@Test
	public void stopsConvertingDepletedElements() {
		Atmosphere atmosphere = new Atmosphere();
		
		double converted = atmosphere.convert(OXYGEN);
		assertEquals(0.33, converted, 0.01);

		for (int i = 0; i < Atmosphere.getInitialElementLevel() - 1; i++)
			atmosphere.convert(OXYGEN);
		
		converted = atmosphere.convert(OXYGEN);
		assertEquals(0, converted, 0.0);
	}
}
