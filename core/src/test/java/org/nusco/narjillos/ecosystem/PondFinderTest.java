package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.FoodPiece;
import org.nusco.narjillos.shared.physics.Vector;

public class PondFinderTest {
	
	Ecosystem pond = new Ecosystem(1000);
	FoodPiece foodPiece1 = pond.spawnFood(Vector.cartesian(100, 100));
	FoodPiece foodPiece2 = pond.spawnFood(Vector.cartesian(1000, 1000));
	FoodPiece foodPiece3 = pond.spawnFood(Vector.cartesian(10000, 10000));
	Narjillo swimmer1 = pond.spawnNarjillo(Vector.cartesian(150, 150), DNA.random());
	Narjillo swimmer2 = pond.spawnNarjillo(Vector.cartesian(1050, 1050), DNA.random());
	
	@Test
	public void findsTheClosestFoodToAGivenPosition() {
		assertEquals(foodPiece1, pond.findFoodPiece(Vector.cartesian(150, 150)));
		assertEquals(foodPiece2, pond.findFoodPiece(Vector.cartesian(900, 900)));
	}

	@Test
	public void findsTheClosestNarjilloToAGivenPosition() {
		assertTrue(pond.findNarjillo(Vector.cartesian(100, 100)).getPosition().almostEquals(Vector.cartesian(150, 150)));
		assertTrue(pond.findNarjillo(Vector.cartesian(1000, 1000)).getPosition().almostEquals(Vector.cartesian(1050, 1050)));
	}

	@Test
	public void returnsNullIfLookingForThingsInAThinglessWorld() {
		Ecosystem pond = new Ecosystem(1000);
		
		assertNull(pond.findFoodPiece(Vector.cartesian(150, 150)));
		assertNull(pond.findNarjillo(Vector.cartesian(150, 150)));
	}
}
