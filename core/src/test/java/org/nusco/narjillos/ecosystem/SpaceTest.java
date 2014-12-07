package org.nusco.narjillos.ecosystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;

public class SpaceTest {

	Space space = new Space(10_000);

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
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		Thing thing = things[0]; // area [5, 3]
		List<Set<Thing>> neighbors = space.getNeighbors(thing);

		assertEquals(4, neighbors.size());

		Set<Thing> area4_2 = neighbors.get(0);
		assertEquals(1, area4_2.size());
		assertEquals("2", area4_2.iterator().next().getLabel());

		Set<Thing> area5_3 = neighbors.get(1);
		assertEquals(1, area5_3.size());
		assertEquals("1", area5_3.iterator().next().getLabel());

		Set<Thing> area6_3 = neighbors.get(2);
		assertEquals(2, area6_3.size());
		Iterator<Thing> area6_3Iterator = area6_3.iterator();
		assertEquals("3", area6_3Iterator.next().getLabel());
		assertEquals("4", area6_3Iterator.next().getLabel());

		Set<Thing> area6_4 = neighbors.get(3);
		assertEquals(1, area6_4.size()); // the thing itself is not included
		assertEquals("5", area6_4.iterator().next().getLabel());
	}

	@Test
	public void canRemoveThings() {
		MockThing thingThatStays = new MockThing(Vector.cartesian(510, 310), 0); // area [5, 3]
		MockThing thingThatGoes = new MockThing(Vector.cartesian(520, 320), 1);  // area [5, 3]
		Thing[] things = new Thing[] {
				thingThatStays,
				thingThatGoes
		};
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		space.remove(thingThatGoes);
		
		assertFalse(space.contains(thingThatGoes));
	}

	@Test
	public void knowsWhetherAThingExists() {
		MockThing thing = new MockThing(Vector.cartesian(100, 200), 1);

		assertFalse(space.contains(thing));

		space.add(thing);

		assertTrue(space.contains(thing));
	}
	
	@Test
	public void neighborsSearchIgnoresTheSearchedThing() {
		Thing[] things = new Thing[] {
				new MockThing(Vector.cartesian(510, 510), 0), // area [5, 5]
				new MockThing(Vector.cartesian(520, 520), 1), // area [5, 5]
		};
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		Thing referenceThing = things[0]; // area [5, 5]
		List<Set<Thing>> neighbors = space.getNeighbors(referenceThing);

		assertEquals(1, neighbors.size());

		Set<Thing> area5_5 = neighbors.get(0);
		assertEquals(1, area5_5.size());
		assertEquals("1", area5_5.iterator().next().getLabel());
	}
	
	@Test
	public void neighborsSearchWorksForThingsThatAreNotInTheSpace() {
		MockThing thingOutOfSpace = new MockThing(Vector.cartesian(510, 510), 0); // area [5, 5]
		space.add(new MockThing(Vector.cartesian(520, 520), 1)); // area [5, 5]

		List<Set<Thing>> neighbors = space.getNeighbors(thingOutOfSpace);

		assertEquals(1, neighbors.size());

		Set<Thing> area5_5 = neighbors.get(0);
		assertEquals(1, area5_5.size());
		assertEquals("1", area5_5.iterator().next().getLabel());
	}

	@Test
	public void neighborsSearchWorksForEdgesAreas() {
		Thing[] things = new Thing[] {
				new MockThing(Vector.cartesian(010, 310), 0), // area [0, 3]
				new MockThing(Vector.cartesian(020, 320), 1), // area [0, 3]
				new MockThing(Vector.cartesian(010, 210), 2), // area [0, 2]
				new MockThing(Vector.cartesian(110, 410), 3), // area [1, 4]
		};
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		Thing referenceThing = things[0]; // area [0, 3]
		List<Set<Thing>> neighbors = space.getNeighbors(referenceThing);

		assertEquals(3, neighbors.size());

		Set<Thing> area0_2 = neighbors.get(0);
		assertEquals(1, area0_2.size());
		assertEquals("2", area0_2.iterator().next().getLabel());

		Set<Thing> area0_3 = neighbors.get(1);
		assertEquals(1, area0_3.size());
		assertEquals("1", area0_3.iterator().next().getLabel());

		Set<Thing> area1_4 = neighbors.get(2);
		assertEquals(1, area1_4.size());
		assertEquals("3", area1_4.iterator().next().getLabel());
	}

	@Test
	public void neighborsSearchWorksForCornerAreas() {
		Thing[] things = new Thing[] {
				new MockThing(Vector.cartesian(9910, 9910), 0), // area [99, 99]
				new MockThing(Vector.cartesian(9920, 9920), 1), // area [99, 99]
				new MockThing(Vector.cartesian(9810, 9910), 2), // area [98, 99]
				new MockThing(Vector.cartesian(9910, 9810), 3), // area [99, 98]
		};
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		Thing referenceThing = things[0]; // area [99, 99]
		List<Set<Thing>> neighbors = space.getNeighbors(referenceThing);

		assertEquals(3, neighbors.size());

		Set<Thing> area98_99 = neighbors.get(0);
		assertEquals(1, area98_99.size());
		assertEquals("2", area98_99.iterator().next().getLabel());

		Set<Thing> area99_98 = neighbors.get(1);
		assertEquals(1, area99_98.size());
		assertEquals("3", area99_98.iterator().next().getLabel());

		Set<Thing> area99_99 = neighbors.get(2);
		assertEquals(1, area99_99.size());
		assertEquals("1", area99_99.iterator().next().getLabel());
	}

	@Test
	public void thingsInOuterSpaceBelongToACommonArea() {
		Thing[] things = new Thing[] {
				new MockThing(Vector.cartesian(-1, -1), 0),     // outer space
				new MockThing(Vector.cartesian(10_010, 10_010), 1), // outer space
		};
		for (int i = 0; i < things.length; i++)
			space.add(things[i]);

		Thing referenceThing = things[0]; // outer space
		List<Set<Thing>> neighbors = space.getNeighbors(referenceThing);

		assertEquals(1, neighbors.size());

		Set<Thing> outerSpaceArea = neighbors.get(0);
		assertEquals(1, outerSpaceArea.size());
		assertEquals("1", outerSpaceArea.iterator().next().getLabel());
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
	public String getLabel() {
		return label;
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
}