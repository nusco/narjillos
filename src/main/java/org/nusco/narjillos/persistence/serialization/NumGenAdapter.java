package org.nusco.narjillos.persistence.serialization;

import com.google.gson.*;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.FoodPellet;

import java.lang.reflect.Type;

class NumGenAdapter implements JsonSerializer<NumGen>, JsonDeserializer<NumGen> {

	@Override
	public JsonElement serialize(NumGen numGen, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("seed1", numGen.getSeed1());
		jsonObject.addProperty("seed2", numGen.getSeed2());
		jsonObject.addProperty("serial", numGen.getSerial());
		return jsonObject;
	}

	@Override
	public NumGen deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		long seed1 = jsonObject.get("seed1").getAsLong();
		long seed2 = jsonObject.get("seed2").getAsLong();
		long serial = jsonObject.get("serial").getAsLong();

        return new NumGen(seed1, seed2, serial);
	}
}