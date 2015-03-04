package org.nusco.narjillos.serializer;

import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Fiber;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.things.Thing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {

	private static final Gson gson = new GsonBuilder()
		.registerTypeAdapter(Vector.class, new VectorAdapter())
		.registerTypeAdapter(DNA.class, new DNAAdapter())
		.registerTypeAdapter(Nerve.class, new NerveAdapter())
		.registerTypeAdapter(Fiber.class, new FiberAdapter())
		.registerTypeAdapter(Energy.class, new EnergyAdapter())
		.registerTypeAdapter(Organ.class, new OrganAdapter())
		.registerTypeAdapter(ConnectedOrgan.class, new OrganAdapter())
		.registerTypeAdapter(MovingOrgan.class, new OrganAdapter())
		.registerTypeAdapter(Thing.class, new ThingAdapter())
		.registerTypeAdapter(Ecosystem.class, new EcosystemAdapter())
		.enableComplexMapKeySerialization()
        .create();

	public static <T> String toJson(Object obj, Class<T> clazz) {
		return gson.toJson(obj, clazz);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}
