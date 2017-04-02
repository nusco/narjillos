package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Set;
import java.util.stream.Collectors;

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
		Thing thing = new TestThing(Vector.cartesian(1000, 2000));

		space.add(thing);

		assertThat(space.getAll(thing.getLabel()), contains(thing));
	}

	@Test
	public void returnsThingsByLabel() {
		Thing thing1 = new TestThing(Vector.cartesian(1, 1)) {

			@Override
			public String getLabel() {
				return "a";
			}
		};
		Thing thing2 = new TestThing(Vector.cartesian(2, 2)) {

			@Override
			public String getLabel() {
				return "b";
			}
		};
		Thing thing3 = new TestThing(Vector.cartesian(1000, 1000)) {

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

	@Test(expected=RuntimeException.class)
	public void throwsExceptionIfAThingIsTooLargeForTheCurrentGrid() {
		Thing thing = mock(Thing.class);
		when(thing.getRadius()).thenReturn(HashedLocation.GRID_SIZE + 1d);

		space.add(thing);
	}

	@Test
	public void removesThings() {
		Thing thing = new TestThing(Vector.cartesian(1, 1));

		space.add(thing);
		space.remove(thing);

		assertThat(space.getAll(""), is(emptyCollectionOf(Thing.class)));
		assertThat(space.getHashedLocationsOf(thing).isPresent(), is(false));
		assertThat(space.getThingsAtHashedLocation(1, 1), is(emptyCollectionOf(Thing.class)));
	}

	@Test
	public void thingsHaveHashedLocations() {
		Thing punctiformThing = new TestThing(Vector.cartesian(-1000, 4000));

		space.add(punctiformThing);

		assertThat(space.getHashedLocationsOf(punctiformThing).get(), contains(HashedLocation.at(-3, 11)));
	}

	@Test
	public void aThingCanSpanOverMultipleLocations() {
		Thing thing = mock(Thing.class);
		when(thing.getBoundingBox()).thenReturn(new BoundingBox(-10, 10, 390, 410));

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
		Thing punctiformThing1 = new TestThing(Vector.cartesian(1200, 4000));
		Thing punctiformThing2 = new TestThing(Vector.cartesian(1300, 4100));
		Thing punctiformThing3 = new TestThing(Vector.cartesian(1700, 4100));

		space.add(punctiformThing1);
		space.add(punctiformThing2);
		space.add(punctiformThing3);

		assertThat(space.getThingsAtHashedLocation(4, 11), contains(punctiformThing1, punctiformThing2));
	}

	// TODO: the next tests come from the old "grid" space concept. Review. Are these features still the best way to do things?

	@Test
	public void returnsAllTheThings() {
		Thing thing1 = new TestThing(Vector.cartesian(1, 1));
		Thing thing2 = new TestThing(Vector.cartesian(2, 2));
		Thing thing3 = new TestThing(Vector.cartesian(1000, 1000));

		space.add(thing1);
		space.add(thing2);
		space.add(thing3);

		assertThat(space.getAll("thing"), contains(thing1, thing2, thing3));
	}

	@Test
	public void storesAndRetrievesThingsBasedOnTheirPosition() {
		Thing[] things = new Thing[] {
			new TestThing(Vector.cartesian(680, 410)), // area [5, 3]
			new TestThing(Vector.cartesian(520, 420)), // area [5, 3]
			new TestThing(Vector.cartesian(550, 280)), // area [4, 2]
			new TestThing(Vector.cartesian(810, 410)), // area [6, 3]
			new TestThing(Vector.cartesian(820, 420)), // area [6, 3]
			new TestThing(Vector.cartesian(810, 550)), // area [6, 4]
			new TestThing(Vector.cartesian(1300, 1300)), // area [8, 8]
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
			new TestThing(Vector.cartesian(510, 510)), // area [5, 5]
			new TestThing(Vector.cartesian(520, 520)), // area [5, 5]
		};
		for (Thing thing : things)
			space.add(thing);

		Thing referenceThing = things[0]; // area [5, 5]
		Set<Thing> neighbors = space.getNearbyNeighbors(referenceThing, "");

		assertThat(neighbors.size(), is(1));
		assertThat(neighbors.iterator().next().getPosition(), is(Vector.cartesian(520, 520)));
	}

	@Test
	public void neighborsSearchWorksForThingsThatAreNotInTheSpace() {
		TestThing thingOutOfSpace = new TestThing(Vector.cartesian(510, 510)); // area [5, 5]
		space.add(new TestThing(Vector.cartesian(520, 520))); // area [5, 5]

		Set<Thing> neighbors = space.getNearbyNeighbors(thingOutOfSpace, "");

		assertFalse(space.contains(thingOutOfSpace));

		assertThat(neighbors.size(), is(1));
		assertThat(neighbors.iterator().next().getPosition(), is(Vector.cartesian(520, 520)));
	}

	@Test
	public void identifiesSpecificThings() {
		TestThing thing = new TestThing(Vector.cartesian(100, 200));

		assertFalse(space.contains(thing));

		space.add(thing);

		assertTrue(space.contains(thing));
	}
}
