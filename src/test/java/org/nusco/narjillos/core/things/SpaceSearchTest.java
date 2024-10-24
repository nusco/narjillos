package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Random;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class SpaceSearchTest {

	Space space = new Space();
	Random random = new Random(12345);

	@Test
	public void findsNeighboringThings() {
		Thing[] things = new Thing[7];
		things[0] = new TestThing(at(1, -1));
		things[1] = new TestThing(at(1, -1));
		things[2] = new TestThing(at(-1, -2));
		things[3] = new TestThing(at(2, -1));
		things[4] = new TestThing(at(2, -1));
		things[5] = new TestThing(at(2, 1));
		things[6] = new TestThing(at(3, 2));

		for (Thing thing : things)
			space.add(thing);

		Thing thing = things[0];
		Set<Thing> neighbors = space.getNearbyNeighbors(thing, "");

		assertThat(neighbors, contains(things[2], things[1], things[3], things[4], things[5]));
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
