package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.Vector;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.experiment.environment.FoodPellet;
import org.nusco.narjillos.genomics.DNA;

public class JSONThingSerializationTest {

	@Test
	public void serializesAndDeserializesFoodPellet() {
		Thing food = new FoodPellet(Vector.cartesian(10, 20));

		String json = JSON.toJson(food, Thing.class);
		Thing deserialized = JSON.fromJson(json, Thing.class);

		assertThat(deserialized.getPosition()).isEqualTo(food.getPosition());
		assertThat(deserialized.getBoundingBox()).isEqualTo(food.getBoundingBox());
	}

	@Test
	public void serializesAndDeserializesEggs() {
		var dna = new DNA(1, "{1_2}");
		var egg = new Egg(dna, Vector.cartesian(10, 20), Vector.polar(10, 15), 101, new NumGen(1));

		String json = JSON.toJson(egg, Thing.class);
		Egg deserialized = (Egg) JSON.fromJson(json, Thing.class);

		assertThat(egg.getDNA().toString()).isEqualTo(dna.toString());

		assertThat(deserialized.getPosition()).isEqualTo(egg.getPosition());
		assertThat(deserialized.getVelocity()).isEqualTo(egg.getVelocity());
		assertThat(deserialized.getEnergy().getValue()).isEqualTo(egg.getEnergy().getValue());
		assertThat(deserialized.getIncubationTime()).isEqualTo(egg.getIncubationTime());
	}

	@Test
	public void serializesAndDeserializesNarjillos() {
		var genes = "{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}" +
			"{001_002_003_004_005_006_007_008_009_010_011_012_013_014}";

		var dna = new DNA(1, genes);
		var narjillo = new Narjillo(dna, Vector.cartesian(10, 20), 90, new LifeFormEnergy(1000, 10_000));
		narjillo.setTarget(Vector.cartesian(100, 200));

		for (int i = 0; i < 10; i++)
			narjillo.tick();

		String json = JSON.toJson(narjillo, Thing.class);
		Narjillo deserialized = (Narjillo) JSON.fromJson(json, Thing.class);

		narjillo.tick();
		deserialized.tick();

		assertThat(deserialized.getPosition()).isEqualTo(narjillo.getPosition());
		assertThat(deserialized.getDNA().toString()).isEqualTo(genes);
		assertThat(deserialized.getTarget()).isEqualTo(Vector.cartesian(100, 200));
		assertThat(deserialized.getEnergy().getValue()).isEqualTo(narjillo.getEnergy().getValue());
		assertThat(deserialized.getMouth().getDirection()).isEqualTo(narjillo.getMouth().getDirection());
		assertThat(deserialized.getBoundingBox()).isEqualTo(narjillo.getBoundingBox());

		List<ConnectedOrgan> organs = narjillo.getOrgans();
		List<ConnectedOrgan> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertThat(deserializedOrgans.get(i).getLength()).isEqualTo(organs.get(i).getLength());
	}
}
