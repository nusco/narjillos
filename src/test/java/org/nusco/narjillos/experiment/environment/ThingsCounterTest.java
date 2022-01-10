package org.nusco.narjillos.experiment.environment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ThingsCounterTest {

	final ThingsCounter thingsCounter = new ThingsCounter();

	@Test
	public void addsThingsByLabel() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.add("label2");

		assertThat(thingsCounter.count("label1")).isEqualTo(2);
	}

	@Test
	public void canRemoveThings() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.remove("label1");

		assertThat(thingsCounter.count("label1")).isEqualTo(1);
	}

	@Test
	public void returnsZeroIfNoThingWasEverAdded() {
		assertThat(thingsCounter.count("label1")).isZero();
	}

	@Test
	public void failsIfTryingToCountInTheNegatives() {

		assertThatThrownBy(() -> thingsCounter.remove("label1"))
			.isInstanceOf(RuntimeException.class);
	}
}
