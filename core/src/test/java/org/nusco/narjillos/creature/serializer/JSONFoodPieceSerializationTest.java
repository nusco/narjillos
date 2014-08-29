package org.nusco.narjillos.creature.serializer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.ecosystem.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

public class JSONFoodPieceSerializationTest {

	@Test
	public void serializesAndDeserializesFoodPieces() {
		Thing food = new FoodPiece();

		String json = JSON.toJson(food);
		Thing deserialized = JSON.fromJson(json, FoodPiece.class);
		
		assertEquals(food.getPosition(), deserialized.getPosition());
	}
}
