package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.utilities.RanGen;

public class EcosystemFinderTest {
	
	Ecosystem ecosystem;
	FoodPiece foodPiece1;
	FoodPiece foodPiece2;
	Narjillo narjillo1;
	Narjillo narjillo2;
	RanGen ranGen;
	
	@Before
	public void initialize() {
		ecosystem = new Ecosystem(1000);
		foodPiece1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
		foodPiece2 = ecosystem.spawnFood(Vector.cartesian(999, 999));
		ranGen = new RanGen(1234);
		narjillo1 = insertNarjillo(Vector.cartesian(101, 101));
		narjillo2 = insertNarjillo(Vector.cartesian(998, 998));
	}

	private Narjillo insertNarjillo(Vector position) {
		DNA dna = DNA.random(ranGen);
		Narjillo result = new Narjillo(dna, new Embryo(dna).develop(), position, 10000);
		ecosystem.insertNarjillo(result);
		return result;
	}
	
	@Test
	public void findsTheClosestFoodToAGivenNarjillo() {
		assertEquals(foodPiece1.getPosition(), ecosystem.findClosestFoodPiece(narjillo1));
		assertEquals(foodPiece2.getPosition(), ecosystem.findClosestFoodPiece(narjillo2));
	}
	
	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		Ecosystem emptyEcosystem = new Ecosystem(1000);
		Narjillo narjillo = insertNarjillo(Vector.cartesian(100, 100));
		Vector target = emptyEcosystem.findClosestFoodPiece(narjillo);
		assertEquals(Vector.cartesian(500, 500), target);
	}

	@Test
	public void findsTheClosestNarjilloToAGivenPosition() {
		assertTrue(ecosystem.findNarjillo(Vector.cartesian(100, 100)).getPosition().almostEquals(Vector.cartesian(101, 101)));
		assertTrue(ecosystem.findNarjillo(Vector.cartesian(950, 980)).getPosition().almostEquals(Vector.cartesian(998, 998)));
	}

	@Test
	public void returnsNullIfLookingForNarjillosInAWorldWithoutThem() {
		Ecosystem ecosystem = new Ecosystem(1000);
		
		assertNull(ecosystem.findNarjillo(Vector.cartesian(150, 150)));
	}
}
