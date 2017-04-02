package org.nusco.narjillos.experiment.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;

public class EcosystemTest {

	private Ecosystem ecosystem;

	private FoodPellet foodPellet1;

	private FoodPellet foodPellet2;

	private Narjillo narjillo1;

	private Narjillo narjillo2;

	private final NumGen numGen = new NumGen(1234);

	@Before
	public void initialize() {
		ecosystem = new Ecosystem(1000, false);
		foodPellet1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
		foodPellet2 = ecosystem.spawnFood(Vector.cartesian(1000, 1000));
		ecosystem.spawnFood(Vector.cartesian(10000, 10000));
		narjillo1 = insertNarjillo(Vector.cartesian(150, 150));
		narjillo2 = insertNarjillo(Vector.cartesian(1050, 1050));
	}

	private Narjillo insertNarjillo(Vector position) {
		DNA dna = DNA.random(1, numGen);
		Narjillo result = new Narjillo(dna, position, 90, Energy.INFINITE);
		ecosystem.insert(result);
		return result;
	}

	@Test
	public void returnsAllTheThings() {
		List<Thing> things = ecosystem.getAll("");

		assertEquals(5, things.size());
		assertTrue(things.contains(narjillo1));
		assertTrue(things.contains(foodPellet1));
	}

	@Test
	public void countsThings() {
		assertEquals(3, ecosystem.getCount(FoodPellet.LABEL));
		assertEquals(2, ecosystem.getCount(Narjillo.LABEL));
	}

	@Test
	public void returnsASubsetOfTheThings() {
		List<Thing> things = ecosystem.getAll(FoodPellet.LABEL);

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
		assertEquals(foodPellet1.getPosition(), ecosystem.findClosestFoodTo(narjillo1));
		assertEquals(foodPellet2.getPosition(), ecosystem.findClosestFoodTo(narjillo2));
	}

	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		Ecosystem emptyEcosystem = new Ecosystem(1000, false);
		Narjillo narjillo = insertNarjillo(Vector.cartesian(100, 100));
		Vector target = emptyEcosystem.findClosestFoodTo(narjillo);
		assertEquals(Vector.cartesian(500, 500), target);
	}
}
