package org.nusco.narjillos.persistence.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

abstract class HierarchyAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

	protected abstract String getTypeTag(T obj);

	protected abstract Class<?> getClass(String typeTag) throws JsonParseException;

	protected void preSerialize(T obj) {
	}

	protected void postDeserialize(T obj) {
	}

	@Override
	public JsonElement serialize(T obj, Type type, JsonSerializationContext context) {
		preSerialize(obj);
		JsonObject result = new JsonObject();
		result.addProperty("type", getTypeTag(obj));
		result.add("data", context.serialize(obj));
		return result;
	}

	@Override
	public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		String specificType = jsonObject.get("type").getAsString();
		T result = context.deserialize(jsonObject.get("data"), getClass(specificType));
		postDeserialize(result);
		return result;
	}

	protected Class<?> getClassForName(String className) throws JsonParseException {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e.getMessage());
		}
	}
}