package org.nusco.swimmers.pond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondTest {
	
	Pond pond = new Pond(1000);
	FoodPiece foodPiece1 = pond.spawnFood(Vector.cartesian(100, 100));
	FoodPiece foodPiece2 = pond.spawnFood(Vector.cartesian(1000, 1000));
	FoodPiece foodPiece3 = pond.spawnFood(Vector.cartesian(10000, 10000));
	Narjillo swimmer1 = pond.spawnSwimmer(Vector.cartesian(150, 150), DNA.random());
	Narjillo swimmer2 = pond.spawnSwimmer(Vector.cartesian(1050, 1050), DNA.random());

	@Before
	public void tickPondOnce() {
		pond.tick();
	}
	
	@Test
	public void countsFoodPieces() {
		assertEquals(3, pond.getNumberOfFoodPieces());
	}

	@Test
	public void countsNarjillos() {
		assertEquals(2, pond.getNumberOfNarjillos());
	}

	@Test
	public void returnsAllTheThings() {
		List<? extends Thing> swimmers = pond.getThings();
		
		assertTrue(swimmers.contains(swimmer1));
		assertTrue(swimmers.contains(foodPiece1));
	}
	
	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = {false};
		pond.addEventListener(new PondEventListener() {

			@Override
			public void thingAdded(Thing thing) {
				eventFired[0] = true;
			}

			@Override
			public void thingRemoved(Thing thing) {
			}
		});
		
		pond.spawnFood(Vector.ZERO);
		assertTrue(eventFired[0]);
	}
}
