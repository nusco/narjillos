package org.nusco.narjillos.persistence.serialization;

import org.nusco.narjillos.experiment.environment.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;

import com.google.gson.JsonParseException;

class ThingAdapter extends HierarchyAdapter<Thing> {

	@Override
	protected String getTypeTag(Thing obj) {
		return obj.getLabel();
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		switch (typeTag) {
		case FoodPellet.LABEL:
			return FoodPellet.class;
		case Egg.LABEL:
			return Egg.class;
		case Narjillo.LABEL:
			return Narjillo.class;
		}
		throw new RuntimeException("Unknown subtype of Thing: " + typeTag);
	}
}
