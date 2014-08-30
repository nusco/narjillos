package org.nusco.narjillos.creature.serializer;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.things.Thing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {

	private static final Gson gson = new GsonBuilder()
		.registerTypeAdapter(Nerve.class, new NerveAdapter())
		.registerTypeAdapter(BodyPart.class, new BodyPartAdapter())
		.registerTypeAdapter(Organ.class, new BodyPartAdapter())
		.registerTypeAdapter(Thing.class, new ThingAdapter())
        .setPrettyPrinting()
        .create();

	public static <T> String toJson(Object obj, Class<T> clazz) {
		return gson.toJson(obj, clazz);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}
