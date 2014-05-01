package org.nusco.swimmer.pond;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;
import org.nusco.swimmer.things.Food;

public class PondTest {

	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		Pond pond = new Pond();
		Food food1 = new Food();
		Food food2 = new Food();

		pond.add(food1, 100, 100);
		pond.add(food2, 1000, 1000);
		
		assertEquals(Vector.cartesian(100, 100), pond.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.closestFoodTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestSwimmerToAGivenPosition() {
		Pond pond = new Pond();
		Swimmer swimmer1 = new Swimmer(null);
		Swimmer swimmer2 = new Swimmer(null);

		pond.add(swimmer1, 100, 100);
		pond.add(swimmer2, 1000, 1000);
		
		assertEquals(Vector.cartesian(100, 100), pond.closestSwimmerTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.closestSwimmerTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void returnsTheOriginIfLookingForThingsInAThinglessWorld() {
		Pond pond = new Pond();
		
		assertEquals(Vector.ZERO, pond.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.ZERO, pond.closestSwimmerTo(Vector.cartesian(150, 150)));
	}
}
