package org.nusco.narjillos.experiment.environment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.Energy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.DNA;

public class EcosystemTest {

	private Ecosystem ecosystem;

	private FoodPellet foodPellet1;
	private FoodPellet foodPellet2;

	private Narjillo narjillo1;
	private Narjillo narjillo2;

	private final NumGen numGen = new NumGen(1234);

	@BeforeEach
	public void initialize() {
		ecosystem = new Ecosystem(1000, false);
		foodPellet1 = ecosystem.spawnFood(Vector.cartesian(100, 100));
		foodPellet2 = ecosystem.spawnFood(Vector.cartesian(1000, 1000));
		ecosystem.spawnFood(Vector.cartesian(10000, 10000));
		narjillo1 = insertNarjillo(Vector.cartesian(150, 150));
		narjillo2 = insertNarjillo(Vector.cartesian(1050, 1050));
	}

	private Narjillo insertNarjillo(Vector position) {
		var dna = DNA.random(1, numGen);
		var result = new Narjillo(dna, position, 90, Energy.INFINITE);
		ecosystem.insert(result);
		return result;
	}

	@Test
	public void returnsAllTheThings() {
		List<Thing> things = ecosystem.getAll("");

		assertThat(things).hasSize(5);
		assertThat(things).contains(narjillo1);
		assertThat(things).contains(foodPellet1);
	}

	@Test
	public void countsThings() {
		assertThat(ecosystem.getCount(FoodPellet.LABEL)).isEqualTo(3);
		assertThat(ecosystem.getCount(Narjillo.LABEL)).isEqualTo(2);
	}

	@Test
	public void returnsASubsetOfTheThings() {
		List<Thing> things = ecosystem.getAll(FoodPellet.LABEL);

		assertThat(things).hasSize(3);
		assertThat(things).contains(foodPellet1);
	}

	@Test
	public void sendsEventsWhenAddingThings() {
		final boolean[] eventFired = { false };

		ecosystem.addEventListener(new EnvironmentEventListener() {
			@Override
			public void added(Thing thing) {
				eventFired[0] = true;
			}

			@Override
			public void removed(Thing thing) {
			}
		});
		ecosystem.spawnFood(Vector.ZERO);

		assertThat(eventFired[0]).isTrue();
	}

	@Test
	public void findsTheClosestFoodToAGivenNarjillo() {
		assertThat(ecosystem.findClosestFoodTo(narjillo1)).isEqualTo(foodPellet1.getPosition());
		assertThat(ecosystem.findClosestFoodTo(narjillo2)).isEqualTo(foodPellet2.getPosition());
	}

	@Test
	public void pointsAtCenterOfEcosystemIfThereIsNoFood() {
		var emptyEcosystem = new Ecosystem(1000, false);
		var narjillo = insertNarjillo(Vector.cartesian(100, 100));

		Vector target = emptyEcosystem.findClosestFoodTo(narjillo);

		assertThat(target).isEqualTo(Vector.cartesian(500, 500));
	}
}
