package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HashedSpaceTest {

	@Test
	public void addsThings() {
		Thing thing = new TestThing(Vector.cartesian(1000, 2000), 123);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing);

		assertThat(hashedSpace.getThings(), contains(thing));
	}

	@Test
	public void thingsHaveHashedLocations() {
		Thing punctiformThing = mock(Thing.class);
		when(punctiformThing.getBoundingBox()).thenReturn(BoundingBox.punctiform(Vector.cartesian(-1000, 3000)));

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(punctiformThing);

		assertThat(hashedSpace.getHashedLocationsOf(punctiformThing), contains(HashedLocation.at(-4, 11)));
	}

	@Test
	public void aThingCanSpanMultipleLocations() {
		Thing thing = mock(Thing.class);
		when(thing.getBoundingBox()).thenReturn(new BoundingBox(-10, 10, 290, 310));

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing);

		final Set<HashedLocation> hashedLocationsOf = hashedSpace.getHashedLocationsOf(thing);
		assertThat(hashedLocationsOf, containsInAnyOrder(
			HashedLocation.at(-1, 1),
			HashedLocation.at(1, 1),
			HashedLocation.at(-1, 2),
			HashedLocation.at(1, 2)
			));
	}

	@Test
	public void aLocationCanContainMultipleThings() {
		Thing punctiformThing1 = new TestThing(Vector.cartesian(1000, 3000), 1);
		Thing punctiformThing2 = new TestThing(Vector.cartesian(1100, 3100), 2);
		Thing punctiformThing3 = new TestThing(Vector.cartesian(1500, 3100), 3);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(punctiformThing1);
		hashedSpace.add(punctiformThing2);
		hashedSpace.add(punctiformThing3);

		assertThat(hashedSpace.getThingsAtHashedLocation(4, 11), containsInAnyOrder(punctiformThing1, punctiformThing2));
	}
}
