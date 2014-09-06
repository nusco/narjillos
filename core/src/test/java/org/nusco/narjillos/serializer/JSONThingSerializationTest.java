package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.BodyPart;
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
	public void serializesAndDeserializesNarjillos() {
		String genes = "{001_002_003_004_005_006_007_008}{001_002_003_004_005_006_007_008}{001_002_003_004_005_006_007_008}{001_002_003_004_005_006_007_008}{001_002_003_004_005_006_007_008}";
		DNA dna = new DNA(genes);
		Narjillo narjillo = new Narjillo(dna, new Embryo(dna).develop(), Vector.cartesian(10, 20));
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

		List<BodyPart> bodyParts = narjillo.getBodyParts();
		List<BodyPart> deserializedBodyParts = deserialized.getBodyParts();
		for (int i = 0; i < bodyParts.size(); i++)
			assertEquals(bodyParts.get(i).getLength(), deserializedBodyParts.get(i).getLength());
	}
}
