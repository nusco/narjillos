package org.nusco.narjillos.core.things;

import org.junit.Test;
import org.nusco.narjillos.core.geometry.Vector;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class HashedSpaceTest {

	@Test
	public void addsThings() {
		Thing thing = new PunctiformTestThing(Vector.cartesian(1000, 2000), 123);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing);

		assertThat(hashedSpace.getThings(), contains(thing));
	}

	@Test
	public void eachThingOccupiesHashedLocations() {
		Thing thing = new PunctiformTestThing(Vector.cartesian(-1000, 3000), 123);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing);

		assertThat(hashedSpace.getHashedLocationsOf(thing), contains(new HashedLocation(-3, 10)));
	}

	@Test
	public void aLargeThingCanOccupyMultipleLocations() {
		Thing largeThing = new Thing();

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing);

		assertThat(hashedSpace.getHashedLocationsOf(thing), contains(new HashedLocation(-3, 10)));
	}

	@Test
	public void thingsCanShareLocations() {
		Thing thing1 = new PunctiformTestThing(Vector.cartesian(1000, 3000), 1);
		Thing thing2 = new PunctiformTestThing(Vector.cartesian(1100, 3100), 2);
		Thing thing3 = new PunctiformTestThing(Vector.cartesian(1500, 3100), 3);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing1);
		hashedSpace.add(thing2);
		hashedSpace.add(thing3);

		assertThat(hashedSpace.getHashedLocationsOf(thing1), is(hashedSpace.getHashedLocationsOf(thing2)));
		assertThat(hashedSpace.getHashedLocationsOf(thing1), is(not(hashedSpace.getHashedLocationsOf(thing3))));
	}

	@Test
	public void findsAllThingsInALocation() {
		Thing thing1 = new PunctiformTestThing(Vector.cartesian(1000, 3000), 1);
		Thing thing2 = new PunctiformTestThing(Vector.cartesian(1100, 3100), 2);
		Thing thing3 = new PunctiformTestThing(Vector.cartesian(1500, 3100), 3);

		HashedSpace hashedSpace = new HashedSpace();
		hashedSpace.add(thing1);
		hashedSpace.add(thing2);
		hashedSpace.add(thing3);

		assertThat(hashedSpace.getThingsAtHashedLocation(3, 10), containsInAnyOrder(thing1, thing2));
	}
}
