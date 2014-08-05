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

public class WaterDropTest {
	
	Ecosystem drop = new Ecosystem(1000);
	FoodPiece foodPiece1 = drop.spawnFood(Vector.cartesian(100, 100));
	FoodPiece foodPiece2 = drop.spawnFood(Vector.cartesian(1000, 1000));
	FoodPiece foodPiece3 = drop.spawnFood(Vector.cartesian(10000, 10000));
	Narjillo swimmer1 = drop.spawnNarjillo(Vector.cartesian(150, 150), DNA.random());
	Narjillo swimmer2 = drop.spawnNarjillo(Vector.cartesian(1050, 1050), DNA.random());

	@Before
	public void tickEcosystemOnce() {
		drop.tick();
	}
	
	@Test
	public void countsFoodPieces() {
		assertEquals(3, drop.getNumberOfFoodPieces());
	}

	@Test
	public void countsNarjillos() {
		assertEquals(2, drop.getNumberOfNarjillos());
	}

	@Test
	public void returnsAllTheThings() {
		Set<Thing> things = drop.getThings();
		
		assertTrue(things.contains(swimmer1));
		assertTrue(things.contains(foodPiece1));
	}
	
	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = {false};
		drop.addEventListener(new EcosystemEventListener() {

			@Override
			public void thingAdded(Thing thing) {
				eventFired[0] = true;
			}

			@Override
			public void thingRemoved(Thing thing) {
			}
		});
		
		drop.spawnFood(Vector.ZERO);
		assertTrue(eventFired[0]);
	}
}
