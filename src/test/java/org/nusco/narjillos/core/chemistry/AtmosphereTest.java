package org.nusco.narjillos.core.chemistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.nusco.narjillos.core.chemistry.Element.*;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.configuration.Configuration;

public class AtmosphereTest {

	private static final double MARGIN = 0.01;
	private static final double MARGIN_SMALL = 0.0001;

	@Test
	public void hasElementAmountsAtStart() {
		var atmosphere = new Atmosphere(10, 0);

		assertThat(atmosphere.getAmountOf(OXYGEN)).isEqualTo(10, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(HYDROGEN)).isEqualTo(10, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(NITROGEN)).isEqualTo(10, within(MARGIN_SMALL));
	}

	@Test
	public void hasEqualDensitiesAtStart() {
		var atmosphere = new Atmosphere(10, 0);

		assertThat(atmosphere.getDensityOf(OXYGEN)).isEqualTo(0.33, within(MARGIN));
		assertThat(atmosphere.getDensityOf(HYDROGEN)).isEqualTo(0.33, within(MARGIN));
		assertThat(atmosphere.getDensityOf(NITROGEN)).isEqualTo(0.33, within(MARGIN));
	}

	@Test
	public void theDefaultInitialLevelIsReadFromConfiguration() {
		var atmosphere = new Atmosphere();

		assertThat(atmosphere.getAmountOf(OXYGEN)).isEqualTo(Configuration.ECOSYSTEM_INITIAL_ELEMENT_LEVEL);
	}

	@Test
	public void hasNoElementZero() {
		var atmosphere = new Atmosphere(10, 0);

		assertThat(atmosphere.getAmountOf(ZERO)).isZero();
		assertThat(atmosphere.getDensityOf(ZERO)).isZero();
	}

	@Test
	public void hasAFixedCatalystLevel() {
		var atmosphere = new Atmosphere(10, 15);

		assertThat(atmosphere.getCatalystLevel()).isEqualTo(15);
	}

	@Test
	public void theSumOfTheDensitiesIsAlwaysOne() {
		var atmosphere = new Atmosphere(10, 0);

		for (int i = 0; i < 3; i++) {
			atmosphere.convert(HYDROGEN, NITROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}

		double sumOfDensities = atmosphere.getDensityOf(OXYGEN) + atmosphere.getDensityOf(HYDROGEN) + atmosphere.getDensityOf(NITROGEN);

		assertThat(sumOfDensities).isEqualTo(1.0, within(MARGIN_SMALL));
	}

	@Test
	public void convertsElements() {
		var atmosphere = new Atmosphere(10, 0);

		atmosphere.convert(OXYGEN, HYDROGEN);

		assertThat(atmosphere.getAmountOf(OXYGEN)).isEqualTo(9, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(HYDROGEN)).isEqualTo(11, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(NITROGEN)).isEqualTo(10, within(MARGIN_SMALL));
	}

	@Test
	public void densitiesNeverGetBelowZeroOrAboveOne() {
		var atmosphere = new Atmosphere(10, 0);

		for (int i = 0; i < 30; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(NITROGEN, HYDROGEN);
		}

		assertThat(atmosphere.getDensityOf(OXYGEN)).isEqualTo(0.0, within(MARGIN_SMALL));
		assertThat(atmosphere.getDensityOf(HYDROGEN)).isEqualTo(1.0, within(MARGIN_SMALL));
		assertThat(atmosphere.getDensityOf(NITROGEN)).isEqualTo(0.0, within(MARGIN_SMALL));
	}

	@Test
	public void neverConvertsFromTheZeroElement() {
		var atmosphere = new Atmosphere(10, 0);

		atmosphere.convert(ZERO, HYDROGEN);

		assertThat(atmosphere.getAmountOf(ZERO)).isZero();
		assertThat(atmosphere.getAmountOf(HYDROGEN)).isEqualTo(10.0, within(MARGIN_SMALL));
	}

	@Test
	public void neverConvertsToTheZeroElement() {
		var atmosphere = new Atmosphere(10, 0);

		atmosphere.convert(OXYGEN, ZERO);

		assertThat(atmosphere.getAmountOf(OXYGEN)).isEqualTo(10.0, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(ZERO)).isZero();
	}

	@Test
	public void stopsConvertingDepletedElements() {
		Atmosphere atmosphere = new Atmosphere(10, 0);

		for (int i = 0; i < 20; i++) {
			atmosphere.convert(OXYGEN, HYDROGEN);
			atmosphere.convert(OXYGEN, NITROGEN);
		}

		assertThat(atmosphere.getAmountOf(OXYGEN)).isZero();
		assertThat(atmosphere.getAmountOf(HYDROGEN)).isEqualTo(15.0, within(MARGIN_SMALL));
		assertThat(atmosphere.getAmountOf(NITROGEN)).isEqualTo(15.0, within(MARGIN_SMALL));
	}
}
