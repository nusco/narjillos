package org.nusco.narjillos.persistence.file;

import java.lang.reflect.Type;

import org.nusco.narjillos.genomics.DNA;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class DNAAdapter implements JsonSerializer<DNA>, JsonDeserializer<DNA> {

	@Override
	public JsonElement serialize(DNA dna, Type type, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("genes", dna.toString());
        jsonObject.addProperty("id", dna.getId());
        jsonObject.addProperty("parentId", dna.getParentId());
        return jsonObject;
	}

	@Override
	public DNA deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		String genes = jsonObject.get("genes").getAsString();
		long id = jsonObject.get("id").getAsLong();
		long parentId = jsonObject.get("parentId").getAsLong();
		return new DNA(id, genes, parentId);
	}
}
