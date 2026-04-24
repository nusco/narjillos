package org.nusco.narjillos.experiment.environment;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ThingsCounterTest {

	ThingsCounter thingsCounter = new ThingsCounter();

	@Test
	public void addsThingsByLabel() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.add("label2");

		assertThat(thingsCounter.count("label1"), is(2L));
	}

	@Test
	public void canRemoveThings() {
		thingsCounter.add("label1");
		thingsCounter.add("label1");
		thingsCounter.remove("label1");

		assertThat(thingsCounter.count("label1"), is(1L));
	}

	@Test
	public void returnsZeroIfNoThingWasEverAdded() {
		assertThat(thingsCounter.count("label1"), is(0L));
	}

	@Test(expected = RuntimeException.class)
	public void failsIfTryingToCountInTheNegatives() {
		thingsCounter.remove("label1");
	}
}
