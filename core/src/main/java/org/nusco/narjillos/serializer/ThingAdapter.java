package org.nusco.narjillos.serializer;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Placemark;
import org.nusco.narjillos.shared.things.Thing;

import com.google.gson.JsonParseException;

class ThingAdapter extends HierarchyAdapter<Thing> {

	@Override
	protected String getTypeTag(Thing obj) {
		return obj.getLabel();
	}

	@Override
	protected Class<?> getClass(String typeTag) throws JsonParseException {
		if (typeTag.equals("food_piece"))
			return FoodPiece.class;
		if (typeTag.equals("narjillo"))
			return Narjillo.class;
		if (typeTag.equals("placemark"))
			return Placemark.class;
		throw new RuntimeException("Unknown subtype of Thing: " + typeTag);
	}
}
