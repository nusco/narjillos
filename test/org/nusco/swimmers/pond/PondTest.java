package org.nusco.swimmers.pond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.shared.physics.Vector;
import org.nusco.swimmers.shared.things.Thing;

public class PondTest {
	
	Pond pond = new Pond();
	Food food1 = new Food();
	Food food2 = new Food();
	Swimmer swimmer1 = new Swimmer(new Head(0, 0, 0));
	Swimmer swimmer2 = new Swimmer(new Head(0, 0, 0));

	@Before
	public void setUpPond() {
		pond.add(food1, 100, 100);
		pond.add(food2, 1000, 1000);

		pond.add(swimmer1, 100, 100);
		pond.add(swimmer2, 1000, 1000);
	}

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
	public void returnsTheOriginIfLookingForThingsInAThinglessWorld() {
		Pond pond = new Pond();
		
		assertEquals(Vector.ZERO, pond.find("food", Vector.cartesian(150, 150)));
		assertEquals(Vector.ZERO, pond.find("swimmer", Vector.cartesian(150, 150)));
	}

	@Test
	public void returnsAllTheThings() {
		Set<Thing> swimmers = pond.getThings();
		
		assertTrue(swimmers.contains(swimmer1));
		assertTrue(swimmers.contains(food1));
	}
}
