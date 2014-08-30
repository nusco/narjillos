package org.nusco.narjillos.creature.serializer;

import java.lang.reflect.Type;

import org.nusco.narjillos.creature.genetics.DNA;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DNAAdapter implements JsonSerializer<DNA>, JsonDeserializer<DNA> {

	@Override
	public JsonElement serialize(DNA dna, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(dna.toString());
	}

	@Override
	public DNA deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		return new DNA(json.getAsJsonPrimitive().getAsString());
	}
}
