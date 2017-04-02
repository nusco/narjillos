package org.nusco.narjillos.experiment.environment;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoodPelletTest {

	FoodPellet foodPellet = new FoodPellet(Vector.ZERO);

	@Test
	public void diesWhenSomeoneEatsIt() {
		assertThat(foodPellet.isDead(), is(false));

		Thing feeder = mock(Thing.class);
		when(feeder.getEnergy()).thenReturn(Energy.INFINITE);

		foodPellet.getEaten(feeder);

		assertThat(foodPellet.isDead(), is(true));
	}
}
