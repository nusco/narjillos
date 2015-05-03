package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.FoodPiece;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;

public class EcosystemTest {
	
	Ecosystem ecosystem;
	FoodPiece foodPiece1;
	FoodPiece foodPiece2;
	FoodPiece foodPiece3;
	Narjillo narjillo1;
	Narjillo narjillo2;
	RanGen ranGen = new RanGen(1234);
	
	@Before
	public void initialize() {
		ecosystem = new Ecosystem(1000, false);
		foodPiece1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
		foodPiece2 = ecosystem.spawnFood(Vector.cartesian(1000, 1000));
		foodPiece3 = ecosystem.spawnFood(Vector.cartesian(10000, 10000));
		narjillo1 = insertNarjillo(Vector.cartesian(150, 150));
		narjillo2 = insertNarjillo(Vector.cartesian(1050, 1050));
	}

	private Narjillo insertNarjillo(Vector position) {
		DNA dna = DNA.random(1, ranGen);
		Narjillo result = new Narjillo(dna, position, 90, Energy.INFINITE);
		ecosystem.insertNarjillo(result);
		return result;
	}

	@Test
	public void countsFoodPieces() {
		assertEquals(3, ecosystem.getNumberOfFoodPieces());
	}

	@Test
	public void countsNarjillos() {
		assertEquals(2, ecosystem.getNumberOfNarjillos());
	}

	@Test
	public void returnsAllTheThings() {
		Set<Thing> things = ecosystem.getThings("");
		
		assertEquals(5, things.size());
		assertTrue(things.contains(narjillo1));
		assertTrue(things.contains(foodPiece1));
	}

	@Test
	public void returnsAllNarjillos() {
		Set<Thing> things = ecosystem.getThings("narjillo");
		
		assertEquals(2, things.size());
		assertTrue(things.contains(narjillo1));
	}

	@Test
	public void returnsASubsetOfThings() {
		Set<Thing> things = ecosystem.getThings("food_piece");
		
		assertEquals(3, things.size());
		assertTrue(things.contains(foodPiece1));
	}
	
	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = {false};
		ecosystem.addEventListener(new EnvironmentEventListener() {

			@Override
			public void thingAdded(Thing thing) {
				eventFired[0] = true;
			}

			@Override
			public void thingRemoved(Thing thing) {
			}
		});
		
		ecosystem.spawnFood(Vector.ZERO);
		assertTrue(eventFired[0]);
	}
	
	@Test
	public void findsTheClosestFoodToAGivenNarjillo() {
		assertEquals(foodPiece1.getPosition(), ecosystem.findClosestFoodPiece(narjillo1));
		assertEquals(foodPiece2.getPosition(), ecosystem.findClosestFoodPiece(narjillo2));
	}

	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		Ecosystem emptyEcosystem = new Ecosystem(1000, false);
		Narjillo narjillo = insertNarjillo(Vector.cartesian(100, 100));
		Vector target = emptyEcosystem.findClosestFoodPiece(narjillo);
		assertEquals(Vector.cartesian(500, 500), target);
	}
}
