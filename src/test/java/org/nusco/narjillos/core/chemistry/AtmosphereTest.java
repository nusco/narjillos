package org.nusco.narjillos.core.chemistry;

import static org.junit.Assert.assertEquals;
import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;
import static org.nusco.narjillos.core.chemistry.Element.ZERO;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.utilities.Configuration;

public class AtmosphereTest {

	@Test
	public void hasElementAmountsAtStart() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		assertEquals(10, atmosphere.getAmountOf(OXYGEN), 0.0);
		assertEquals(10, atmosphere.getAmountOf(HYDROGEN), 0.0);
		assertEquals(10, atmosphere.getAmountOf(NITROGEN), 0.0);
	}

	@Test
	public void hasEqualDensitiesAtStart() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		assertEquals(0.33, atmosphere.getDensityOf(OXYGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(HYDROGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(NITROGEN), 0.01);
	}

	@Test
	public void theDefaultInitialLevelIsReadFromConfiguration() {
		Atmosphere atmosphere = new Atmosphere();
		
		assertEquals(Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL, atmosphere.getAmountOf(OXYGEN), 0.0);
	}

	@Test
	public void hasElementDensities() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		assertEquals(0.33, atmosphere.getDensityOf(OXYGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(HYDROGEN), 0.01);
		assertEquals(0.33, atmosphere.getDensityOf(NITROGEN), 0.01);
	}

	@Test
	public void hasNoElementZero() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		assertEquals(0, atmosphere.getAmountOf(ZERO));
		assertEquals(0.0, atmosphere.getDensityOf(ZERO), 0.0);
	}
	
	@Test
	public void hasAFixedCatalystLevel() {
		Atmosphere atmosphere = new Atmosphere(10, 15);

		assertEquals(15, atmosphere.getCatalystLevel());
	}

	@Test
	public void theSumOfTheDensitiesIsAlwaysOne() {
		Atmosphere atmosphere = new Atmosphere(10, 0);

		for (int i = 0; i < 3; i++) {
			atmosphere.convert(HYDROGEN, NITROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}
		
		double sumOfDensities = atmosphere.getDensityOf(OXYGEN) + atmosphere.getDensityOf(HYDROGEN) + atmosphere.getDensityOf(NITROGEN);
		assertEquals(1.0, sumOfDensities, 0.0001);
	}
	
	@Test
	public void convertsElements() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		atmosphere.convert(OXYGEN, HYDROGEN);
		
		assertEquals(9, atmosphere.getAmountOf(OXYGEN));
		assertEquals(11, atmosphere.getAmountOf(HYDROGEN), 0.0);
		assertEquals(10, atmosphere.getAmountOf(NITROGEN), 0.0);
	}

	@Test
	public void densitiesNeverGetBelowZeroOrAboveOne() {
		Atmosphere atmosphere = new Atmosphere(10, 0);

		for (int i = 0; i < 30; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(NITROGEN, HYDROGEN);
		}
		
		assertEquals(0.0, atmosphere.getDensityOf(OXYGEN), 0.0001);
		assertEquals(1.0, atmosphere.getDensityOf(HYDROGEN), 0.0001);
		assertEquals(0.0, atmosphere.getDensityOf(NITROGEN), 0.0001);
	}
	
	@Test
	public void neverConvertsFromTheZeroElement() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		atmosphere.convert(ZERO, HYDROGEN);
		
		assertEquals(0, atmosphere.getAmountOf(ZERO));
		assertEquals(10, atmosphere.getAmountOf(HYDROGEN));
	}
	
	@Test
	public void neverConvertsToTheZeroElement() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		atmosphere.convert(OXYGEN, ZERO);
		
		assertEquals(10, atmosphere.getAmountOf(OXYGEN));
		assertEquals(0, atmosphere.getAmountOf(ZERO));
	}
	
	@Test
	public void stopsConvertingDepletedElements() {
		Atmosphere atmosphere = new Atmosphere(10, 0);
		
		for (int i = 0; i < 20; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}
		
		assertEquals(0, atmosphere.getAmountOf(OXYGEN), 0.0);
		assertEquals(15, atmosphere.getAmountOf(HYDROGEN), 0.0);
		assertEquals(15, atmosphere.getAmountOf(NITROGEN), 0.0);
	}
}
