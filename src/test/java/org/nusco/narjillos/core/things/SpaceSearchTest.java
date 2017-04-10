package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Random;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class SpaceSearchTest {

	Space space = new Space();
	Random random = new Random(12345);

	@Test
	public void findsNeighboringThings() {
		// translate to area around 0
		Thing[] things = new Thing[] {
			new TestThing(at(5, 3)),
			new TestThing(at(5, 3)),
			new TestThing(at(4, 2)),
			new TestThing(at(6, 3)),
			new TestThing(at(6, 3)),
			new TestThing(at(6, 4)),
			new TestThing(at(8, 8))
		};
		for (Thing thing : things)
			space.add(thing);

		Thing thing = things[0];
		Set<Thing> neighbors = space.getNearbyNeighbors(thing, "");

		assertThat(neighbors, contains(things[2], things[1], things[3], things[4], things[5]));
	}

	@Test
	public void neighborsSearchIgnoresTheSearchedThing() {
		Thing[] things = new Thing[] {
			new TestThing(at(5, 5)),
			new TestThing(at(5, 5))
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0];
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertThat(neighbors, contains(things[1]));
	}

	@Test
	public void theReferenceThingNeedsNotBeInTheSpace() {
		TestThing thingOutOfSpace = new TestThing(Vector.cartesian(at(5), at(5)));

		TestThing thingInSpace = new TestThing(at(5, 5));
		space.add(thingInSpace);

		Set<Thing> neighbors = space.getNearbyNeighbors(thingOutOfSpace, "");

		assertFalse(space.contains(thingOutOfSpace));

		assertThat(neighbors, contains(thingInSpace));
	}

	// A pseudorandom location in square "n, m" of the grid
	private Vector at(int gridX, int gridY) {
		return Vector.cartesian(at(gridX), at(gridY));
	}

	// A pseudorandom location in square "n" of the grid
	private double at(int gridCoordinate) {
		int gridMultiplier = gridCoordinate > 0 ? gridCoordinate - 1 : gridCoordinate;
		double firstCoordinateLocation = HashedLocation.GRID_SIZE * gridMultiplier;
		double offset = HashedLocation.GRID_SIZE * random.nextInt(100) * 0.01;
		return firstCoordinateLocation + offset;
	}
}
