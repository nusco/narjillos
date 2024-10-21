package org.nusco.narjillos.persistence.serialization;

import java.lang.reflect.Type;

import org.nusco.narjillos.core.geometry.Vector;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class VectorAdapter implements JsonSerializer<Vector>, JsonDeserializer<Vector> {

	@Override
	public JsonElement serialize(Vector vector, Type type, JsonSerializationContext context) {
		return context.serialize("(" + vector.x + ", " + vector.y + ")", String.class);
	}

	@Override
	public Vector deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		String vectorDescription = context.deserialize(json, String.class);
		String[] cartesianCoordinates = vectorDescription.replace("(", "").replace(")", "").split(", ");
		double x = Double.parseDouble(cartesianCoordinates[0]);
		double y = Double.parseDouble(cartesianCoordinates[1]);
		return Vector.cartesian(x, y);
	}
}
