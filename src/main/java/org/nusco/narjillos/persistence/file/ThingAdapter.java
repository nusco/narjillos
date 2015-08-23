package org.nusco.narjillos.persistence.file;

import org.nusco.narjillos.core.things.FoodPellet;
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
		if (typeTag.equals("food_pellet"))
			return FoodPellet.class;
		if (typeTag.equals("egg"))
			return Egg.class;
		if (typeTag.equals("narjillo"))
			return Narjillo.class;
		throw new RuntimeException("Unknown subtype of Thing: " + typeTag);
	}
}
