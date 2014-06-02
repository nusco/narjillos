package org.nusco.swimmers.pond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.physics.Vector;
import org.nusco.swimmers.pond.Pond;

public class PondTest {
	
	Pond pond = new Pond();
	Food food1 = new Food();
	Food food2 = new Food();
	Swimmer swimmer1 = new Swimmer(null);
	Swimmer swimmer2 = new Swimmer(null);

	@Before
	public void setUpPond() {
		pond.add(food1, 100, 100);
		pond.add(food2, 1000, 1000);

		pond.add(swimmer1, 100, 100);
		pond.add(swimmer2, 1000, 1000);
	}

	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		assertEquals(Vector.cartesian(100, 100), pond.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.closestFoodTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestSwimmerToAGivenPosition() {
		assertEquals(Vector.cartesian(100, 100), pond.closestSwimmerTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.closestSwimmerTo(Vector.cartesian(900, 900)));
	}

	@Test
	public void returnsTheOriginIfLookingForThingsInAThinglessWorld() {
		Pond pond = new Pond();
		
		assertEquals(Vector.ZERO, pond.closestFoodTo(Vector.cartesian(150, 150)));
		assertEquals(Vector.ZERO, pond.closestSwimmerTo(Vector.cartesian(150, 150)));
	}

	@Test
	public void returnsAllTheSwimmers() {
		Set<Object> swimmers = pond.getSwimmers();
		
		assertTrue(swimmers.contains(swimmer1));
		assertTrue(swimmers.contains(swimmer2));
	}

	@Test
	public void returnsAllTheFood() {
		Set<Object> swimmers = pond.getFood();
		
		assertTrue(swimmers.contains(food1));
		assertTrue(swimmers.contains(food2));
	}
}
