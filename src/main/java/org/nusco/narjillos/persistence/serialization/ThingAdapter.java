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
        return switch (typeTag) {
            case FoodPellet.LABEL -> FoodPellet.class;
            case Egg.LABEL -> Egg.class;
            case Narjillo.LABEL -> Narjillo.class;
            default -> throw new RuntimeException("Unknown subtype of Thing: " + typeTag);
        };
    }
}
