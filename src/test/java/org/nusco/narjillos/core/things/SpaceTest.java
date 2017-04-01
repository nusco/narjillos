package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpaceTest {

	Space space = new Space();

	@Test
	public void addsThings() {
		Thing thing = new TestThing(Vector.cartesian(1000, 2000), 123);

		space.add(thing);

		assertThat(space.getThings(), contains(thing));
	}

	@Test(expected=RuntimeException.class)
	public void throwsExceptionIfAThingIsTooLargeForTheCurrentGrid() {
		Thing thing = mock(Thing.class);
		when(thing.getRadius()).thenReturn(HashedLocation.GRID_SIZE + 1d);

		space.add(thing);
	}

	@Test
	public void removesThings() {
		Thing thing = new TestThing(Vector.cartesian(1, 1), 123);

		space.add(thing);
		space.remove(thing);

		assertThat(space.getThings(), is(emptyCollectionOf(Thing.class)));
		assertThat(space.getHashedLocationsOf(thing).isPresent(), is(false));
		assertThat(space.getThingsAtHashedLocation(1, 1), is(emptyCollectionOf(Thing.class)));
	}

	@Test
	public void thingsHaveHashedLocations() {
		Thing punctiformThing = new TestThing(Vector.cartesian(-1000, 3000), 1);

		space.add(punctiformThing);

		assertThat(space.getHashedLocationsOf(punctiformThing).get(), contains(HashedLocation.at(-4, 11)));
	}

	@Test
	public void aThingCanSpanOverMultipleLocations() {
		Thing thing = mock(Thing.class);
		when(thing.getBoundingBox()).thenReturn(new BoundingBox(-10, 10, 290, 310));

		space.add(thing);

		final Set<HashedLocation> hashedLocationsOf = space.getHashedLocationsOf(thing).get();
		assertThat(hashedLocationsOf, contains(
			HashedLocation.at(-1, 1),
			HashedLocation.at(-1, 2),
			HashedLocation.at(1, 2),
			HashedLocation.at(1, 1)
		));
	}

	@Test
	public void aLocationCanContainMultipleThings() {
		Thing punctiformThing1 = new TestThing(Vector.cartesian(1000, 3000), 1);
		Thing punctiformThing2 = new TestThing(Vector.cartesian(1100, 3100), 2);
		Thing punctiformThing3 = new TestThing(Vector.cartesian(1500, 3100), 3);

		space.add(punctiformThing1);
		space.add(punctiformThing2);
		space.add(punctiformThing3);

		assertThat(space.getThingsAtHashedLocation(4, 11), contains(punctiformThing1, punctiformThing2));
	}

	// TODO: the next tests come from the old "grid" space concept. Review. Are these features still the best way to do things?

	@Test
	public void returnsAllTheThings() {
		Thing thing1 = new TestThing(Vector.cartesian(1, 1), 1);
		Thing thing2 = new TestThing(Vector.cartesian(2, 2), 2);
		Thing thing3 = new TestThing(Vector.cartesian(1000, 1000), 3);

		space.add(thing1);
		space.add(thing2);
		space.add(thing3);

		assertThat(space.getAll(), contains(thing1, thing2, thing3));
	}

	@Test
	public void returnsAllTheThingsWithAGivenLabel() {
		Thing thing1 = new TestThing(Vector.cartesian(1, 1), 1) {

			@Override
			public String getLabel() {
				return "a";
			}
		};
		Thing thing2 = new TestThing(Vector.cartesian(2, 2), 2) {

			@Override
			public String getLabel() {
				return "b";
			}
		};
		Thing thing3 = new TestThing(Vector.cartesian(1000, 1000), 3) {

			@Override
			public String getLabel() {
				return "a";
			}
		};;

		space.add(thing1);
		space.add(thing2);
		space.add(thing3);

		assertThat(space.getAll("a"), contains(thing1, thing3));
	}

	@Test
	public void knowsWhetherItIsEmpty() {
		assertTrue(space.isEmpty());

		space.add(new TestThing(Vector.cartesian(9910, 9910), 0));

		assertFalse(space.isEmpty());
	}

	@Test
	public void storesAndRetrievesThingsBasedOnTheirPosition() {
		Thing[] things = new Thing[] {
			new TestThing(Vector.cartesian(510, 310), 0), // area [5, 3]
			new TestThing(Vector.cartesian(520, 320), 1), // area [5, 3]
			new TestThing(Vector.cartesian(410, 210), 2), // area [4, 2]
			new TestThing(Vector.cartesian(610, 310), 3), // area [6, 3]
			new TestThing(Vector.cartesian(620, 320), 4), // area [6, 3]
			new TestThing(Vector.cartesian(610, 410), 5), // area [6, 4]
			new TestThing(Vector.cartesian(900, 900), 6), // area [8, 8]
		};
		for (Thing thing1 : things)
			space.add(thing1);

		Thing thing = things[0]; // area [5, 3]
		Set<Thing> neighbors = space.getNearbyNeighbors(thing, "");

		assertThat(neighbors, contains(things[2], things[1], things[3], things[4], things[5]));
	}

	@Test
	public void neighborsSearchIgnoresTheSearchedThing() {
		Thing[] things = new Thing[] {
			new TestThing(Vector.cartesian(510, 510), 0), // area [5, 5]
			new TestThing(Vector.cartesian(520, 520), 1), // area [5, 5]
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // area [5, 5]
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertThat(neighbors.size(), is(1));
		assertThat(neighbors.iterator().next().getLabel(), is("1"));
	}

	@Test
	public void neighborsSearchWorksForThingsThatAreNotInTheSpace() {
		TestThing thingOutOfSpace = new TestThing(Vector.cartesian(510, 510), 0); // area [5, 5]
		space.add(new TestThing(Vector.cartesian(520, 520), 1)); // area [5, 5]

		Set<Thing> neighbors = space.getNearbyNeighbors(thingOutOfSpace, "");

		assertFalse(space.contains(thingOutOfSpace));

		assertThat(neighbors.size(), is(1));
		assertThat(neighbors.iterator().next().getLabel(), is("1"));
	}

	@Test
	public void identifiesSpecificThings() {
		TestThing thing = new TestThing(Vector.cartesian(100, 200), 1);

		assertFalse(space.contains(thing));

		space.add(thing);

		assertTrue(space.contains(thing));
	}
}
