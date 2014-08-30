package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

public class EcosystemFinderTest {
	
	private Ecosystem ecosystem = new Ecosystem(1000);
	private FoodPiece foodPiece1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
	private FoodPiece foodPiece2 = ecosystem.spawnFood(Vector.cartesian(999, 999));
	private Narjillo narjillo1 = ecosystem.spawnNarjillo(Vector.cartesian(101, 101), DNA.random());
	private Narjillo narjillo2 = ecosystem.spawnNarjillo(Vector.cartesian(998, 998), DNA.random());
	
	@Test
	public void findsTheClosestFoodToAGivenNarjillo() {
		assertEquals(foodPiece1, ecosystem.findClosestTarget(narjillo1));
		assertEquals(foodPiece2, ecosystem.findClosestTarget(narjillo2));
	}
	
	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		Ecosystem emptyEcosystem = new Ecosystem(1000);
		Narjillo narjillo = emptyEcosystem.spawnNarjillo(Vector.cartesian(100, 100), DNA.random());
		Thing target = emptyEcosystem.findClosestTarget(narjillo);
		assertEquals("placemark", target.getLabel());
		assertEquals(Vector.cartesian(500, 500), target.getPosition());
	}

	@Test
	public void findsTheClosestNarjilloToAGivenPosition() {
		assertTrue(ecosystem.findNarjillo(Vector.cartesian(100, 100)).getPosition().almostEquals(Vector.cartesian(101, 101)));
		assertTrue(ecosystem.findNarjillo(Vector.cartesian(950, 980)).getPosition().almostEquals(Vector.cartesian(998, 998)));
	}

	@Test
	public void returnsNullIfLookingForNarjillosInAWorldWithoutThem() {
		Ecosystem ecosystem = new Ecosystem(1000) {};
		
		assertNull(ecosystem.findNarjillo(Vector.cartesian(150, 150)));
	}
}
