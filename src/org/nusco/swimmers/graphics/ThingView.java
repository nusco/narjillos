package org.nusco.swimmers.graphics;

import javafx.scene.Node;

import org.nusco.swimmers.creature.Swimmer;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.shared.things.Thing;

public abstract class ThingView {

	public abstract Node toNode();

	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("swimmer"))
			return new SwimmerView((Swimmer) thing);
		else if (thing.getLabel().equals("food")) {
			return new FoodView((Food)thing);
			// TODO
			// result.add(new FoodView((Food)thing))
		} else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}
}
