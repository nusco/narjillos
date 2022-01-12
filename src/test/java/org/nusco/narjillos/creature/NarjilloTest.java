package org.nusco.narjillos.creature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.LifeFormEnergy;

public class NarjilloTest {

	final Energy energy = new LifeFormEnergy(10, Double.MAX_VALUE);
	final Narjillo narjillo = mock(Narjillo.class);

	@BeforeEach
	public void setupMocks() {
		when(narjillo.getEnergy()).thenReturn(energy);
		when(narjillo.isDead()).thenCallRealMethod();
	}

	@Test
	public void diesWhenItsEnergyDropsToZero() {
		assertThat(narjillo.isDead()).isFalse();

		energy.dropToZero();

		assertThat(narjillo.isDead()).isTrue();
	}
}
