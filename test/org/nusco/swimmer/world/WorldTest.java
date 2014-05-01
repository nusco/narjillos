package org.nusco.swimmer.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.swimmer.Swimmer;
import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.things.Food;

public class WorldTest {

	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		World world = new World();
		Food food1 = new Food();
		Food food2 = new Food();

		world.placeAt(food1, 100, 100);
		world.placeAt(food2, 1000, 1000);
		
		assertEquals(Vector.cartesian(100, 100), world.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), world.closestFoodTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestSwimmerToAGivenPosition() {
		World world = new World();
		Swimmer swimmer1 = new Swimmer(null);
		Swimmer swimmer2 = new Swimmer(null);

		world.placeAt(swimmer1, 100, 100);
		world.placeAt(swimmer2, 1000, 1000);
		
		assertEquals(Vector.cartesian(100, 100), world.closestSwimmerTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), world.closestSwimmerTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void returnsTheOriginIfLookingForThingsInAThinglessWorld() {
		World world = new World();
		
		assertEquals(Vector.ZERO, world.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.ZERO, world.closestSwimmerTo(Vector.cartesian(150, 150)));
	}
}
