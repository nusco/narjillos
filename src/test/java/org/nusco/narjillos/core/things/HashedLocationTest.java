package org.nusco.narjillos.core.things;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class HashedLocationTest {

	@Test
	public void canBePlacedDirectly() {
		HashedLocation hashedLocation = HashedLocation.at(10, -100);

		assertThat(hashedLocation.lx).isEqualTo(10L);
		assertThat(hashedLocation.ly).isEqualTo(-100L);
	}

	@Test
	public void hashesCoordinatesToAGrid() {
		assertThat(HashedLocation.ofCoordinates(1, 1)).isEqualTo(HashedLocation.at(1, 1));
		assertThat(HashedLocation.ofCoordinates(299, 400)).isEqualTo(HashedLocation.at(1, 2));
		assertThat(HashedLocation.ofCoordinates(500, 1200)).isEqualTo(HashedLocation.at(2, 4));
	}

	@Test
	public void hashesNegativeCoordinates() {
		assertThat(HashedLocation.ofCoordinates(-1, -1)).isEqualTo(HashedLocation.at(-1, -1));
		assertThat(HashedLocation.ofCoordinates(-299, -400)).isEqualTo(HashedLocation.at(-1, -2));
		assertThat(HashedLocation.ofCoordinates(500, -1200)).isEqualTo(HashedLocation.at(2, -4));
	}

	@Test
	public void hashesZeroCoordinatesToTheFirstGridSquare() {
		assertThat(HashedLocation.ofCoordinates(0, -0)).isEqualTo(HashedLocation.at(1, 1));
	}
}
