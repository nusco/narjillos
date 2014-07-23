package org.nusco.narjillos.pond;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;

public class PondFinderTest {
	
	Pond pond = new Pond(1000);
	FoodPiece foodPiece1 = pond.spawnFood(Vector.cartesian(100, 100));
	FoodPiece foodPiece2 = pond.spawnFood(Vector.cartesian(1000, 1000));
	FoodPiece foodPiece3 = pond.spawnFood(Vector.cartesian(10000, 10000));
	Narjillo swimmer1 = pond.spawnSwimmer(Vector.cartesian(150, 150), DNA.random());
	Narjillo swimmer2 = pond.spawnSwimmer(Vector.cartesian(1050, 1050), DNA.random());
	
	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		assertEquals(Vector.cartesian(100, 100), pond.findFoodPiece(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(1000, 1000), pond.findFoodPiece(Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestNarjilloToAGivenPosition() {
		assertTrue(pond.findNarjillo(Vector.cartesian(100, 100)).almostEquals(Vector.cartesian(150, 150)));
		assertTrue(pond.findNarjillo(Vector.cartesian(1000, 1000)).almostEquals(Vector.cartesian(1050, 1050)));
	}

	@Test
	public void returnsThePondCenterIfLookingForThingsInAThinglessWorld() {
		Pond pond = new Pond(1000);
		
		assertEquals(Vector.cartesian(500, 500), pond.findFoodPiece(Vector.cartesian(150, 150)));
		assertEquals(Vector.cartesian(500, 500), pond.findNarjillo(Vector.cartesian(150, 150)));
	}
}
