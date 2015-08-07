package org.nusco.narjillos.persistence.file;

import java.lang.reflect.Type;

import org.nusco.narjillos.creature.body.Fiber;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class FiberAdapter implements JsonSerializer<Fiber>, JsonDeserializer<Fiber> {

	@Override
	public JsonElement serialize(Fiber fiber, Type type, JsonSerializationContext context) {
		return context.serialize(fiber.toString(), String.class);
	}

	@Override
	public Fiber deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		String fiberDescription = context.deserialize(json, String.class);
		String[] fiberComponents = fiberDescription.replace("(", "").replace(")", "").split("\\, ");
		int red = Integer.parseInt(fiberComponents[0]);
		int green = Integer.parseInt(fiberComponents[1]);
		int blue = Integer.parseInt(fiberComponents[2]);
		return new Fiber(red, green, blue);
	}
}
