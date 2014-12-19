package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.embryogenesis.Embryo;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

public class JSONThingSerializationTest {

	@Test
	public void serializesAndDeserializesFoodPieces() {
		Thing food = new FoodPiece();

		String json = JSON.toJson(food, Thing.class);
		Thing deserialized = JSON.fromJson(json, Thing.class);
		
		assertEquals(food.getPosition(), deserialized.getPosition());
	}

	@Test
	public void serializesAndDeserializesEggs() {
		DNA dna = new DNA("{1_2}");
		Egg egg = new Egg(dna, Vector.cartesian(10, 20), 101, 10);

		String json = JSON.toJson(egg, Thing.class);
		Thing deserialized = JSON.fromJson(json, Thing.class);

		assertEquals(dna.toString(), egg.getDNA().toString());
		assertEquals(egg.getPosition(), deserialized.getPosition());
		assertEquals(egg.getEnergy().getValue(), deserialized.getEnergy().getValue(), 0);
	}

	@Test
	public void serializesAndDeserializesNarjillos() {
		String genes = "{001_002_003_004_005_006_007_008_009}{001_002_003_004_005_006_007_008_009}{001_002_003_004_005_006_007_008_009}{001_002_003_004_005_006_007_008_009}{001_002_003_004_005_006_007_008_009}";
		DNA dna = new DNA(genes);
		Narjillo narjillo = new Narjillo(dna, new Embryo(dna).develop(), Vector.cartesian(10, 20), 10000);
		narjillo.setTarget(Vector.cartesian(100, 200));
		for (int i = 0; i < 10; i++)
			narjillo.tick();
		
		String json = JSON.toJson(narjillo, Thing.class);
		Narjillo deserialized = (Narjillo) JSON.fromJson(json, Thing.class);
		
		narjillo.tick();
		deserialized.tick();
		
		assertEquals(narjillo.getPosition(), deserialized.getPosition());
		assertEquals(genes, deserialized.getDNA().toString());
		assertEquals(Vector.cartesian(100, 200), deserialized.getTarget());
		assertEquals(narjillo.getEnergy().getValue(), deserialized.getEnergy().getValue(), 0.0);
		assertEquals(narjillo.getMouth(), deserialized.getMouth());

		List<Organ> organs = narjillo.getOrgans();
		List<Organ> deserializedOrgans = deserialized.getOrgans();
		for (int i = 0; i < organs.size(); i++)
			assertEquals(organs.get(i).getLength(), deserializedOrgans.get(i).getLength(), 0);
	}
}
