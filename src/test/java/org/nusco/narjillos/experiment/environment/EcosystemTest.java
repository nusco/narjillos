package org.nusco.narjillos.experiment.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.EnvironmentEventListener;
import org.nusco.narjillos.genomics.DNA;

public class EcosystemTest {

	private Ecosystem ecosystem;

	private FoodPellet foodPellet1;

	private FoodPellet foodPellet2;

	private FoodPellet foodPellet3;

	private Narjillo narjillo1;

	private Narjillo narjillo2;

	private final NumGen numGen = new NumGen(1234);

	@Before
	public void initialize() {
		ecosystem = new Ecosystem(1000, false);
		foodPellet1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
		foodPellet2 = ecosystem.spawnFood(Vector.cartesian(1000, 1000));
		foodPellet3 = ecosystem.spawnFood(Vector.cartesian(10000, 10000));
		narjillo1 = insertNarjillo(Vector.cartesian(150, 150));
		narjillo2 = insertNarjillo(Vector.cartesian(1050, 1050));
	}

	private Narjillo insertNarjillo(Vector position) {
		DNA dna = DNA.random(1, numGen);
		Narjillo result = new Narjillo(dna, position, 90, Energy.INFINITE);
		ecosystem.insertNarjillo(result);
		return result;
	}

	@Test
	public void countsFoodPellets() {
		assertEquals(3, ecosystem.getNumberOfFoodPellets());
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
		assertTrue(things.contains(foodPellet1));
	}

	@Test
	public void returnsAllNarjillos() {
		Set<Thing> things = ecosystem.getThings("narjillo");

		assertEquals(2, things.size());
		assertTrue(things.contains(narjillo1));
	}

	@Test
	public void returnsASubsetOfThings() {
		Set<Thing> things = ecosystem.getThings("food_pellet");

		assertEquals(3, things.size());
		assertTrue(things.contains(foodPellet1));
	}

	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = { false };
		ecosystem.addEventListener(new EnvironmentEventListener() {

			@Override
			public void added(Thing thing) {
				eventFired[0] = true;
			}

			@Override
			public void removed(Thing thing) {
			}
		});

		ecosystem.spawnFood(Vector.ZERO);
		assertTrue(eventFired[0]);
	}

	@Test
	public void findsTheClosestFoodToAGivenNarjillo() {
		assertEquals(foodPellet1.getPosition(), ecosystem.findClosestFood(narjillo1));
		assertEquals(foodPellet2.getPosition(), ecosystem.findClosestFood(narjillo2));
	}

	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		Ecosystem emptyEcosystem = new Ecosystem(1000, false);
		Narjillo narjillo = insertNarjillo(Vector.cartesian(100, 100));
		Vector target = emptyEcosystem.findClosestFood(narjillo);
		assertEquals(Vector.cartesian(500, 500), target);
	}
}
