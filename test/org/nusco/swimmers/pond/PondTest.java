package org.nusco.swimmers.pond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.genetics.DNA;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondTest {
	
	Pond pond = new Pond(1000);
	Food food1 = pond.spawnFood(Vector.cartesian(100, 100));
	Food food2 = pond.spawnFood(Vector.cartesian(1000, 1000));
	Swimmer swimmer1 = pond.spawnSwimmer(Vector.cartesian(100, 100), DNA.random());
	Swimmer swimmer2 = pond.spawnSwimmer(Vector.cartesian(1000, 1000), DNA.random());

	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		assertEquals(Vector.cartesian(100, 100), pond.find("food", Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.find("food", Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestSwimmerToAGivenPosition() {
		assertEquals(Vector.cartesian(100, 100), pond.find("swimmer", Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.find("swimmer", Vector.cartesian(900, 900)));
	}

	@Test
	public void returnsThePondCenterIfLookingForThingsInAThinglessWorld() {
		Pond pond = new Pond(1000);
		
		assertEquals(Vector.cartesian(500, 500), pond.find("food", Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(500, 500), pond.find("swimmer", Vector.cartesian(150, 150)));
	}

	@Test
	public void returnsAllTheThings() {
		List<Thing> swimmers = pond.getThings();
		
		assertTrue(swimmers.contains(swimmer1));
		assertTrue(swimmers.contains(food1));
	}

	@Test
	public void returnsASpecificTypeOfThings() {
		List<Thing> swimmers = pond.getThings("swimmer");
		
		assertTrue(swimmers.contains(swimmer1));
		assertFalse(swimmers.contains(food1));
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
