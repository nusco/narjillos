package org.nusco.narjillos.creature;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.genomics.DNA;

public class EggTest {

	private final DNA dna = new DNA(1, "{1_2_3}");

	private final Egg egg = new Egg(dna, Vector.cartesian(10, 20), Vector.ZERO, 100, new NumGen(1));

	@Test
	public void hatchesANarjilloAfterAnIncubationPeriod() {
		assertThat(egg.getHatchedNarjillo().isPresent()).isFalse();

		waitUntilItHatches(egg);
		var narjillo = egg.getHatchedNarjillo().get();

		assertThat(narjillo.getPosition()).isEqualTo(egg.getPosition());
	}

	@Test
	public void itsLastInteractingThingIsInitiallyNull() {
		Thing lastInteractingThing = egg.getInteractor();

		assertThat(lastInteractingThing).isSameAs(Thing.NULL);
	}

	@Test
	public void theHatchedNarjilloBecomesItsInteractor() {
		waitUntilItHatches(egg);

		assertThat(egg.getInteractor()).isSameAs(egg.getHatchedNarjillo().get());
	}

	@Test
	public void passesItsEnergyToTheHatchedNarjillo() {
		assertThat(egg.getEnergy().getValue()).isEqualTo(100.0);

		waitUntilItHatches(egg);
		var narjillo = egg.getHatchedNarjillo().get();

		assertThat(egg.getEnergy().getValue()).isEqualTo(0.0);
		assertThat(narjillo.getEnergy().getValue()).isEqualTo(100.0);
	}

	@Test
	public void putsDNAIntoTheHatchedNarjillo() {
		waitUntilItHatches(egg);
		var narjillo = egg.getHatchedNarjillo().get();

		assertThat(narjillo.getDNA()).isSameAs(dna);
	}

	@Test
	public void onlyHatchesOnce() {
		waitUntilItHatches(egg);

		assertThat(egg.hatch(new NumGen(1))).isFalse();
	}

	@Test
	public void decaysUpTo100PercentAfterHatching() {
		assertThat(egg.isDead()).isFalse();
		assertThat(egg.getFading()).isEqualTo(0.0);

		waitUntilItHatches(egg);

		for (int i = 0; i < 100; i++) {
			assertThat(egg.isDead()).isFalse();
			assertThat(egg.getFading()).isEqualTo(i / 100.0);
			egg.tick();
		}

		assertThat(egg.isDead()).isTrue();
		assertThat(egg.getFading()).isEqualTo(1.0);

		egg.tick();
		assertThat(egg.getFading()).isEqualTo(1.0);
	}

	private void waitUntilItHatches(Egg egg) {
		NumGen numGen = new NumGen(1);
		while (!egg.hatch(numGen))
			egg.tick();
	}
}
