package org.nusco.narjillos.application.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNA;

public class LocatorTest {

	private Ecosystem ecosystem;

	private Locator locator;

	@BeforeEach
	public void initialize() {
		ecosystem = new Ecosystem(1000, false);
		locator = new Locator(ecosystem);
	}

	private Narjillo insertNarjillo(Vector position) {
		// The position specifies the head position; the body center will be different
		var dna = new DNA(1,
			"{145_227_116_072_163_201_077_221_217}{060_227_157_252_209_149_056_114_167}{250_253_092_189_010_247_016_214_009}{027_039_203_179_042_042_175_110_008}");
		var narjillo = new Narjillo(dna, position, 90, Energy.INFINITE);
		ecosystem.insert(narjillo);
		return narjillo;
	}

	@Test
	public void findsThingsAtAGivenPosition() {
		var narjillo1 = insertNarjillo(Vector.cartesian(1000, 1000));
		var narjillo2 = insertNarjillo(Vector.cartesian(100, 100));

		// The narjillos have radius = 2.5
		assertThat(narjillo1.getRadius())
				.isGreaterThan(2)
				.isLessThan(3);
		assertThat(narjillo2.getRadius())
				.isGreaterThan(2)
				.isLessThan(3);

		// point 4.24 units away from head, 3.04 from center
		assertThat(locator.findNarjilloAt(Vector.cartesian(103, 103), 1)).isNull();

		// point 2.82 units away from head, 2.06 from center (-> within the radius)
		assertThat(locator.findNarjilloAt(Vector.cartesian(102, 102), 1)).isEqualTo(narjillo2);
		assertThat(locator.findNarjilloAt(Vector.cartesian(998, 1002), 1)).isEqualTo(narjillo1);
	}

	@Test
	public void findsExpandedNarjillo() {
		var narjillo1 = insertNarjillo(Vector.cartesian(100, 100));

		// original narjillo -> radius = 2.5
		assertThat(locator.findNarjilloAt(Vector.cartesian(110, 110), 1)).isNull();
		// expanded narjillo -> radius = 20
		assertThat(locator.findNarjilloAt(Vector.cartesian(110, 110), 20)).isEqualTo(narjillo1);
	}

	@Test
	public void returnsNullIfNoThingIsCloseEnough() {
		insertNarjillo(Vector.cartesian(100, 10));

		assertThat(locator.findNarjilloAt(Vector.cartesian(500, 500))).isNull();
	}

	@Test
	public void returnsNullIfTheEcosystemContainsNoNarjillos() {
		var emptyLocator = new Locator(new Ecosystem(1000, false));

		assertThat(emptyLocator.findNarjilloAt(Vector.cartesian(150, 150))).isNull();
	}
}
