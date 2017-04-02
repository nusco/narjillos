package org.nusco.narjillos.experiment.environment;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ThingsCounterTest {

	ThingsCounter thingsCounter = new ThingsCounter();

	@Test
	public void addsThingsByLabel() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.add("label2");

		assertThat(thingsCounter.count("label1"), is(2));
	}

	@Test
	public void canRemoveThings() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.remove("label1");

		assertThat(thingsCounter.count("label1"), is(1));
	}

	@Test
	public void returnsZeroIfNoThingWasEverAdded() {
		assertThat(thingsCounter.count("label1"), is(0));
	}

	@Test(expected = RuntimeException.class)
	public void failsIfTryingToCountInTheNegatives() {
		thingsCounter.remove("label1");
	}
}
