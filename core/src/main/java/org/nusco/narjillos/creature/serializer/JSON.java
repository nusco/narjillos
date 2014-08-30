package org.nusco.narjillos.creature.serializer;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.shared.utilities.RanGen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {

	private static final Gson gson = new GsonBuilder()
		.registerTypeAdapter(Vector.class, new VectorAdapter())
		.registerTypeAdapter(DNA.class, new DNAAdapter())
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

	public static String serializeRandomGenerator() {
		return toJson(RanGen.getCurrentSeed(), Long.class);
	}

	public static void deserializeRandomGenerator(String json) {
		Long seed = fromJson(json, Long.class);
		RanGen.initializeWith(seed);
	}
}
