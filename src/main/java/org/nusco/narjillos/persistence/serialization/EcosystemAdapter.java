package org.nusco.narjillos.persistence.serialization;

import java.lang.reflect.Type;

import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.experiment.environment.FoodPellet;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;

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

		final JsonElement foodPellets = context.serialize(ecosystem.getThings("food_pellet"));
		jsonObject.add("foodPellets", foodPellets);

		final JsonElement eggs = context.serialize(ecosystem.getThings("egg"));
		jsonObject.add("eggs", eggs);

		final JsonElement narjillos = context.serialize(ecosystem.getThings("narjillo"));
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

		JsonArray foodPellets = jsonObject.get("foodPellets").getAsJsonArray();
		for (int i = 0; i < foodPellets.size(); i++) {
			JsonElement jsonFoodPellet = foodPellets.get(i);
			FoodPellet foodPellet = context.deserialize(jsonFoodPellet, FoodPellet.class);
			result.insert(foodPellet);
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
