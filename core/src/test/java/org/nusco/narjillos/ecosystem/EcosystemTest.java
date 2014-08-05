package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.FoodPiece;
import org.nusco.narjillos.ecosystem.EcosystemEventListener;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;

public class EcosystemTest {
	
	Ecosystem ecosystem = new Ecosystem(1000);
	FoodPiece foodPiece1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
	FoodPiece foodPiece2 = ecosystem.spawnFood(Vector.cartesian(1000, 1000));
	FoodPiece foodPiece3 = ecosystem.spawnFood(Vector.cartesian(10000, 10000));
	Narjillo swimmer1 = ecosystem.spawnNarjillo(Vector.cartesian(150, 150), DNA.random());
	Narjillo swimmer2 = ecosystem.spawnNarjillo(Vector.cartesian(1050, 1050), DNA.random());

	@Before
	public void tickEcosystemOnce() {
		ecosystem.tick();
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
		Set<Thing> things = ecosystem.getThings();
		
		assertTrue(things.contains(swimmer1));
		assertTrue(things.contains(foodPiece1));
	}
	
	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = {false};
		ecosystem.addEventListener(new EcosystemEventListener() {

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
}
