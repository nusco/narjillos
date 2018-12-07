package org.nusco.narjillos.creature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;

public class NarjilloTest {

	Energy energy = new LifeFormEnergy(10, Double.MAX_VALUE);
	Narjillo narjillo = mock(Narjillo.class);

	@Before
	public void setupMocks() {
		when(narjillo.getEnergy()).thenReturn(energy);
		when(narjillo.isDead()).thenCallRealMethod();
	}

	@Test
	public void diesWhenItsEnergyDropsToZero() {
		assertThat(narjillo.isDead(), is(false));

		energy.dropToZero();

		assertThat(narjillo.isDead(), is(true));
	}
}
