package org.nusco.narjillos.serializer;

import java.lang.reflect.Type;
import java.util.Set;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.GenePool;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

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

        final JsonElement foodPieces = context.serialize(ecosystem.getFoodPieces());
        jsonObject.add("foodPieces", foodPieces);

        final JsonElement genePool = context.serialize(ecosystem.getGenePool());
        jsonObject.add("genePool", genePool);

        return jsonObject;
    }

	@Override
	public Ecosystem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();

	    long size = jsonObject.get("size").getAsLong();
	    Ecosystem result = new Ecosystem(size);
	    
	    JsonArray foodPieces = jsonObject.get("foodPieces").getAsJsonArray();
	    for (int i = 0; i < foodPieces.size(); i++) {
	      JsonElement jsonFoodPiece = foodPieces.get(i);
	      FoodPiece foodPiece = context.deserialize(jsonFoodPiece, FoodPiece.class);
	      result.forceAdd((FoodPiece) foodPiece);
	    }
	    
	    GenePool genePool = context.deserialize(jsonObject.get("genePool"), GenePool.class);
	    Set<Creature> creatures = genePool.getCreatures();
	    for (Creature creature : creatures)
	    	result.forceAdd((Narjillo) creature);
	    
	    return result;
	}
}
