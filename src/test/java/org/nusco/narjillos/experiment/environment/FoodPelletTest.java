package org.nusco.narjillos.experiment.environment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;

public class FoodPelletTest {

	final FoodPellet foodPellet = new FoodPellet(Vector.ZERO);

	@Test
	public void diesWhenSomeoneEatsIt() {
		assertThat(foodPellet.isDead()).isFalse();

		Thing feeder = mock(Thing.class);
		when(feeder.getEnergy()).thenReturn(Energy.INFINITE);

		foodPellet.getEaten(feeder);

		assertThat(foodPellet.isDead()).isTrue();
	}
}
