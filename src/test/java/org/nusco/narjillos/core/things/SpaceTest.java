package org.nusco.narjillos.core.things;

import org.junit.Assert;
import org.junit.Test;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class SpaceTest {

	private final Space space = new Space(10_000);

	@Test
	public void isDividedIntoAGridOfSquareAreas() {
		assertEquals(100, space.getAreaSize(), 0.00001);
	}

	@Test
	public void storesThingsIntoAMatrixOfAreas() {
		Assert.assertArrayEquals(new int[] { 0, 0 }, space.add(new MockThing(Vector.cartesian(0, 0), 0)));
		Assert.assertArrayEquals(new int[] { 99, 99 }, space.add(new MockThing(Vector.cartesian(9_999, 9_999), 0)));

		Assert.assertArrayEquals(new int[] { 0, 0 }, space.add(new MockThing(Vector.cartesian(99, 99), 0)));
		Assert.assertArrayEquals(new int[] { 1, 1 }, space.add(new MockThing(Vector.cartesian(100, 100), 0)));

		Assert.assertArrayEquals(new int[] { 5, 8 }, space.add(new MockThing(Vector.cartesian(510, 810), 0)));
		Assert.assertArrayEquals(new int[] { 8, 5 }, space.add(new MockThing(Vector.cartesian(810, 510), 0)));
	}

	@Test
	public void storesAndRetrievesThingsBasedOnTheirPosition() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(510, 310), 0), // area [5, 3]
			new MockThing(Vector.cartesian(520, 320), 1), // area [5, 3]
			new MockThing(Vector.cartesian(410, 210), 2), // area [4, 2]
			new MockThing(Vector.cartesian(610, 310), 3), // area [6, 3]
			new MockThing(Vector.cartesian(620, 320), 4), // area [6, 3]
			new MockThing(Vector.cartesian(610, 410), 5), // area [6, 4]
			new MockThing(Vector.cartesian(810, 810), 6), // area [8, 8]
		};
		for (Thing thing1 : things)
			space.add(thing1);

		Thing thing = things[0]; // area [5, 3]
		Set<Thing> neighbors = space.getNearbyNeighbors(thing, "");

		assertEquals(5, neighbors.size());
		Iterator<Thing> thingsIterator = neighbors.iterator();

		assertEquals("2", thingsIterator.next().getLabel());
		assertEquals("1", thingsIterator.next().getLabel());
		assertEquals("3", thingsIterator.next().getLabel());
		assertEquals("4", thingsIterator.next().getLabel());
		assertEquals("5", thingsIterator.next().getLabel());
	}

	@Test
	public void removesThings() {
		MockThing thingThatStays = new MockThing(Vector.cartesian(510, 310), 0); // area [5, 3]
		MockThing thingThatGoes = new MockThing(Vector.cartesian(520, 320), 1);  // area [5, 3]
		Thing[] things = new Thing[] {
			thingThatStays,
			thingThatGoes
		};
		for (Thing thing : things)
			space.add(thing);

		space.remove(thingThatGoes);

		assertFalse(space.contains(thingThatGoes));
	}

	@Test
	public void identifiesSpecificThings() {
		MockThing thing = new MockThing(Vector.cartesian(100, 200), 1);

		assertFalse(space.contains(thing));

		space.add(thing);

		assertTrue(space.contains(thing));
	}

	@Test
	public void identifiesSpecificThingsEvenInOuterSpace() {
		MockThing thing = new MockThing(Vector.cartesian(-1000, -2000), 1);

		space.add(thing);

		assertTrue(space.contains(thing));
	}

	@Test
	public void neighborsSearchIgnoresTheSearchedThing() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(510, 510), 0), // area [5, 5]
			new MockThing(Vector.cartesian(520, 520), 1), // area [5, 5]
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // area [5, 5]
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertEquals(1, neighbors.size());
		assertEquals("1", neighbors.iterator().next().getLabel());
	}

	@Test
	public void neighborsSearchWorksForThingsThatAreNotInTheSpace() {
		MockThing thingOutOfSpace = new MockThing(Vector.cartesian(510, 510), 0); // area [5, 5]
		space.add(new MockThing(Vector.cartesian(520, 520), 1)); // area [5, 5]

		Set<Thing> neighbors = space.getNearbyNeighbors(thingOutOfSpace, "");

		assertEquals(1, neighbors.size());
		assertEquals("1", neighbors.iterator().next().getLabel());
	}

	@Test
	public void neighborsSearchWorksForEdgeAreas() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(10, 310), 0), // area [0, 3]
			new MockThing(Vector.cartesian(20, 320), 1), // area [0, 3]
			new MockThing(Vector.cartesian(10, 210), 2), // area [0, 2]
			new MockThing(Vector.cartesian(110, 410), 3), // area [1, 4]
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // area [0, 3]
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertEquals(3, neighbors.size());
		Iterator<Thing> thingsIterator = neighbors.iterator();
		assertEquals("2", thingsIterator.next().getLabel());
		assertEquals("1", thingsIterator.next().getLabel());
		assertEquals("3", thingsIterator.next().getLabel());
	}

	@Test
	public void neighborsSearchWorksForCornerAreas() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(9910, 9910), 0), // area [99, 99]
			new MockThing(Vector.cartesian(9920, 9920), 1), // area [99, 99]
			new MockThing(Vector.cartesian(9810, 9910), 2), // area [98, 99]
			new MockThing(Vector.cartesian(9910, 9810), 3), // area [99, 98]
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // area [99, 99]
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");
		assertEquals(3, neighbors.size());
		Iterator<Thing> thingsIterator = neighbors.iterator();
		assertEquals("2", thingsIterator.next().getLabel());
		assertEquals("3", thingsIterator.next().getLabel());
		assertEquals("1", thingsIterator.next().getLabel());
	}

	@Test
	public void thingsInOuterSpaceBelongToACommonArea() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(-1, -1), 0),     // outer space
			new MockThing(Vector.cartesian(10_010, 10_010), 1), // outer space
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // outer space
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertEquals(1, neighbors.size());
		assertEquals("1", neighbors.iterator().next().getLabel());
	}

	@Test
	public void returnsAllTheThings() {
		Thing[] things = new Thing[] {
			new MockThing(Vector.cartesian(9910, 9910), 0),
			new MockThing(Vector.cartesian(9920, 9920), 1),
			new MockThing(Vector.cartesian(10_010, 10_010), 1),
		};
		for (Thing thing : things)
			space.add(thing);

		Set<Thing> allTheThings = space.getAll("");

		assertEquals(3, allTheThings.size());
		for (Thing thing : things)
			assertTrue(allTheThings.contains(thing));
	}

	@Test
	public void knowsWhetherItIsEmpty() {
		assertTrue(space.isEmpty());

		space.add(new MockThing(Vector.cartesian(9910, 9910), 0));

		assertFalse(space.isEmpty());
	}
}

class MockThing implements Thing {

	private final String label;

	private final Vector position;

	public MockThing(Vector position, Integer id) {
		this.position = position;
		this.label = id.toString();
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public Vector getCenter() {
		return getPosition();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Thing getLastInteractingThing() {
		return Thing.NULL;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	public Segment tick() {
		return null;
	}

	@Override
	public Energy getEnergy() {
		return null;
	}

	@Override
	public double getRadius() {
		return 0;
	}
}