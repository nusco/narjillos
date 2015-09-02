package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.things.LifeFormEnergy;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.persistence.serialization.JSON;

public class JSONThingSerializationTest {

	@Test
	public void serializesAndDeserializesFoodPellet() {
		Thing food = new FoodPellet();

		String json = JSON.toJson(food, Thing.class);
		Thing deserialized = JSON.fromJson(json, Thing.class);
		
		assertEquals(food.getPosition(), deserialized.getPosition());
	}

	@Test
	public void serializesAndDeserializesEggs() {
		DNA dna = new DNA(1, "{1_2}");
		Egg egg = new Egg(dna, Vector.cartesian(10, 20), Vector.polar(10, 15), 101, new NumGen(1));

		String json = JSON.toJson(egg, Thing.class);
		Egg deserialized = (Egg)JSON.fromJson(json, Thing.class);

		assertEquals(dna.toString(), egg.getDNA().toString());
		assertEquals(egg.getPosition(), deserialized.getPosition());
		assertEquals(egg.getVelocity(), deserialized.getVelocity());
		assertEquals(egg.getEnergy().getValue(), deserialized.getEnergy().getValue(), 0);
		assertEquals(egg.getIncubationTime(), deserialized.getIncubationTime(), 0);
	}

	@Test
	public void serializesAndDeserializesNarjillos() {
		Atmosphere atmosphere = new Atmosphere();
		String genes = "{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}{001_002_003_004_005_006_007_008_009_010_011_012_013_014}";
		DNA dna = new DNA(1, genes);
		Narjillo narjillo = new Narjillo(dna, Vector.cartesian(10, 20), 90, new LifeFormEnergy(1000, 10_000));
		narjillo.setTarget(Vector.cartesian(100, 200));
		for (int i = 0; i < 10; i++)
			narjillo.tick(atmosphere);
		
		String json = JSON.toJson(narjillo, Thing.class);
		Narjillo deserialized = (Narjillo) JSON.fromJson(json, Thing.class);
		
		Atmosphere duplicatedAtmosphere = atmosphere.duplicate();
		
		narjillo.tick(atmosphere);
		deserialized.tick(duplicatedAtmosphere);
		
		assertEquals(narjillo.getPosition(), deserialized.getPosition());
		assertEquals(genes, deserialized.getDNA().toString());
		assertEquals(Vector.cartesian(100, 200), deserialized.getTarget());
		assertEquals(narjillo.getEnergy().getValue(), deserialized.getEnergy().getValue(), 0.0);
		assertEquals(narjillo.getMouth().getDirection(), deserialized.getMouth().getDirection());

		List<ConnectedOrgan> organs = narjillo.getOrgans();
		List<ConnectedOrgan> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertEquals(organs.get(i).getLength(), deserializedOrgans.get(i).getLength(), 0);
	}
}
