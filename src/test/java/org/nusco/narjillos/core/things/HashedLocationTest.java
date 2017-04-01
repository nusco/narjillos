package org.nusco.narjillos.core.things;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HashedLocationTest {

	@Test
	public void canBePlacedDirectly() {
		HashedLocation hashedLocation = HashedLocation.at(10, -100);

		assertThat(hashedLocation.lx, is(10L));
		assertThat(hashedLocation.ly, is(-100L));
	}

	@Test
	public void hashesCoordinatesToAGrid() {
		assertThat(HashedLocation.ofCoordinates(1, 1), is(HashedLocation.at(1, 1)));
		assertThat(HashedLocation.ofCoordinates(299, 400), is(HashedLocation.at(1, 2)));
		assertThat(HashedLocation.ofCoordinates(500, 1200), is(HashedLocation.at(2, 4)));
	}

	@Test
	public void hashesNegativeCoordinates() {
		assertThat(HashedLocation.ofCoordinates(-1, -1), is(HashedLocation.at(-1, -1)));
		assertThat(HashedLocation.ofCoordinates(-299, -400), is(HashedLocation.at(-1, -2)));
		assertThat(HashedLocation.ofCoordinates(500, -1200), is(HashedLocation.at(2, -4)));
	}

	@Test
	public void hashesZeroCoordinatesToTheFirstGridSquare() {
		assertThat(HashedLocation.ofCoordinates(0, -0), is(HashedLocation.at(1, 1)));
	}
}
