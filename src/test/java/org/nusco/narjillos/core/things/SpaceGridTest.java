package org.nusco.narjillos.core.things;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Vector;

public class SpaceGridTest {

	final Space space = new Space();

	@Test
	public void addsThings() {
		var thing = new TestThing(Vector.cartesian(1000, 2000));

		space.add(thing);

		assertThat(space.getAll(thing.getLabel())).contains(thing);
	}

	@Test
	public void returnsThingsByLabel() {
		var thing1 = new TestThing(Vector.cartesian(1, 1)) {
			@Override
			public String getLabel() {
				return "a";
			}
		};
		var thing2 = new TestThing(Vector.cartesian(2, 2)) {
			@Override
			public String getLabel() {
				return "b";
			}
		};
		var thing3 = new TestThing(Vector.cartesian(1000, 1000)) {
			@Override
			public String getLabel() {
				return "a";
			}
		};

		space.add(thing1);
		space.add(thing2);
		space.add(thing3);

		assertThat(space.getAll("a")).contains(thing1, thing3);
	}

	@Test
	public void throwsExceptionIfAThingIsTooLargeForTheCurrentGrid() {
		Thing thing = mock(Thing.class);
		when(thing.getRadius()).thenReturn(HashedLocation.GRID_SIZE + 1.0);

		assertThatThrownBy(() -> space.add(thing))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	public void removesThings() {
		Thing thing = new TestThing(Vector.cartesian(1, 1));

		space.add(thing);
		space.remove(thing);

		assertThat(space.getAll("")).isEmpty();
		assertThat(space.getHashedLocationsOf(thing).isPresent()).isFalse();
		assertThat(space.getThingsAtHashedLocation(1, 1)).isEmpty();
	}

	@Test
	public void thingsHaveHashedLocations() {
		Thing punctiformThing = new TestThing(Vector.cartesian(-1000, 4000));

		space.add(punctiformThing);

        assertThat(space.getHashedLocationsOf(punctiformThing).get()).contains(new HashedLocation(-3, 11));
	}

	@Test
	public void aThingCanSpanOverMultipleLocations() {
		Thing thing = mock(Thing.class);
		when(thing.getBoundingBox()).thenReturn(new BoundingBox(-10, 10, 390, 410));

		space.add(thing);

		final Set<HashedLocation> hashedLocationsOf = space.getHashedLocationsOf(thing).get();
        assertThat(hashedLocationsOf).contains(
                new HashedLocation(-1, 1),
                new HashedLocation(-1, 2),
                new HashedLocation(1, 2),
                new HashedLocation(1, 1)
		);
	}

	@Test
	public void aLocationCanContainMultipleThings() {
		Thing punctiformThing1 = new TestThing(Vector.cartesian(1200, 4000));
		Thing punctiformThing2 = new TestThing(Vector.cartesian(1300, 4100));
		Thing punctiformThing3 = new TestThing(Vector.cartesian(1700, 4100));

		space.add(punctiformThing1);
		space.add(punctiformThing2);
		space.add(punctiformThing3);

		assertThat(space.getThingsAtHashedLocation(4, 11)).contains(punctiformThing1, punctiformThing2);
	}

	@Test
	public void returnsAllTheThings() {
		Thing thing1 = new TestThing(Vector.cartesian(1, 1));
		Thing thing2 = new TestThing(Vector.cartesian(2, 2));
		Thing thing3 = new TestThing(Vector.cartesian(1000, 1000));

		space.add(thing1);
		space.add(thing2);
		space.add(thing3);

		assertThat(space.getAll("thing")).contains(thing1, thing2, thing3);
	}

	@Test
	public void identifiesSpecificThings() {
		Thing thing = new TestThing(Vector.cartesian(100, 200));

		assertThat(space.contains(thing)).isFalse();

		space.add(thing);

		assertThat(space.contains(thing)).isTrue();
	}
}
