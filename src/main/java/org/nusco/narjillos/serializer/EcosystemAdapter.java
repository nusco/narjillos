package org.nusco.narjillos.serializer;

import java.lang.reflect.Type;

import org.nusco.narjillos.core.things.FoodPiece;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.chemistry.Atmosphere;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class EcosystemAdapter implements JsonSerializer<Ecosystem>, JsonDeserializer<Ecosystem> {

	@Override
	public JsonElement serialize(Ecosystem ecosystem, Type type, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("size", ecosystem.getSize());

		final JsonElement foodPieces = context.serialize(ecosystem.getThings("food_piece"));
		jsonObject.add("foodPieces", foodPieces);

		final JsonElement eggs = context.serialize(ecosystem.getThings("egg"));
		jsonObject.add("eggs", eggs);

		final JsonElement narjillos = context.serialize(ecosystem.getNarjillos());
		jsonObject.add("narjillos", narjillos);

		final JsonElement atmosphere = context.serialize(ecosystem.getAtmosphere());
		jsonObject.add("atmosphere", atmosphere);

		return jsonObject;
	}

	@Override
	public Ecosystem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

		long size = jsonObject.get("size").getAsLong();
		Ecosystem result = new Ecosystem(size, false);

		JsonArray foodPieces = jsonObject.get("foodPieces").getAsJsonArray();
		for (int i = 0; i < foodPieces.size(); i++) {
			JsonElement jsonFoodPiece = foodPieces.get(i);
			FoodPiece foodPiece = context.deserialize(jsonFoodPiece, FoodPiece.class);
			result.insert(foodPiece);
		}

		JsonArray eggs = jsonObject.get("eggs").getAsJsonArray();
		for (int i = 0; i < eggs.size(); i++) {
			JsonElement jsonEgg = eggs.get(i);
			Egg egg = context.deserialize(jsonEgg, Egg.class);
			result.insert(egg);
		}

		JsonArray narjillos = jsonObject.get("narjillos").getAsJsonArray();
		for (int i = 0; i < narjillos.size(); i++) {
			JsonElement jsonNarjllo = narjillos.get(i);
			Narjillo narjillo = context.deserialize(jsonNarjllo, Narjillo.class);
			result.insertNarjillo(narjillo);
		}

		JsonElement jsonAtmosphere = jsonObject.get("atmosphere");
		Atmosphere atmosphere = context.deserialize(jsonAtmosphere, Atmosphere.class);
		result.setAtmosphere(atmosphere);

		return result;
	}
}
