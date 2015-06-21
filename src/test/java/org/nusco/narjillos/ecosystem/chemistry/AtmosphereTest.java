package org.nusco.narjillos.ecosystem.chemistry;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.ecosystem.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.OXYGEN;

import org.junit.Test;
import org.nusco.narjillos.core.utilities.Configuration;

public class AtmosphereTest {

	@Test
	public void hasAnElementLevelsAtStart() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		assertEquals(10, atmosphere.getElementLevel(OXYGEN), 0.0);
		assertEquals(10, atmosphere.getElementLevel(HYDROGEN), 0.0);
		assertEquals(10, atmosphere.getElementLevel(NITROGEN), 0.0);
	}

	@Test
	public void hasEqualDensitiesAtStart() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		assertEquals(0.33, atmosphere.getDensityOf(OXYGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(HYDROGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(NITROGEN), 0.01);
	}

	@Test
	public void theDefaultInitialLevelIsReadFromConfiguration() {
		Atmosphere atmosphere = new Atmosphere();
		
		assertEquals(Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL, atmosphere.getElementLevel(OXYGEN), 0.0);
	}

	@Test
	public void hasElementDensities() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		assertEquals(0.33, atmosphere.getDensityOf(OXYGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(HYDROGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(NITROGEN), 0.01);
	}
	
	@Test
	public void theSumOfTheDensitiesIsAlwaysOne() {
		Atmosphere atmosphere = new Atmosphere(10);

		for (int i = 0; i < 3; i++) {
			atmosphere.convert(HYDROGEN, NITROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}
		
		double sumOfDensities = atmosphere.getDensityOf(OXYGEN) + atmosphere.getDensityOf(HYDROGEN) + atmosphere.getDensityOf(NITROGEN);
		assertEquals(1.0, sumOfDensities, 0.0001);
	}

	@Test
	public void densitiesNeverGetBelowZeroOrAboveOne() {
		Atmosphere atmosphere = new Atmosphere(10);

		for (int i = 0; i < 30; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(NITROGEN, HYDROGEN);
		}
		
		assertEquals(0.0, atmosphere.getDensityOf(OXYGEN), 0.0001);
		assertEquals(1.0, atmosphere.getDensityOf(HYDROGEN), 0.0001);
		assertEquals(0.0, atmosphere.getDensityOf(NITROGEN), 0.0001);
	}
	
	@Test
	public void convertsElements() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		for (int i = 0; i < 11; i++)
			atmosphere.convert(OXYGEN, HYDROGEN);
		
		assertEquals(0, atmosphere.getElementLevel(OXYGEN));
		assertEquals(20, atmosphere.getElementLevel(HYDROGEN), 0.0);
		assertEquals(10, atmosphere.getElementLevel(NITROGEN), 0.0);
	}
	
	@Test
	public void stopsConvertingDepletedElements() {
		Atmosphere atmosphere = new Atmosphere(10);
		
		for (int i = 0; i < 11; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}
		
		assertEquals(0, atmosphere.getDensityOf(OXYGEN), 0.0);
	}
}
