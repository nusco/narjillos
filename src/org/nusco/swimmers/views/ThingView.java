package org.nusco.swimmers.views;

import javafx.scene.Node;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.shared.things.Thing;

abstract class ThingView {

	private final Thing thing;

	public ThingView(Thing thing) {
		this.thing = thing;
	}

	public Thing getThing() {
		return thing;
	}

	public final Node toNode(Viewport viewport) {
		if (!isVisible(viewport))
			return null;
		
		return toNode();
	}
	
	protected abstract boolean isVisible(Viewport viewport);

	public abstract Node toNode();
	
	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("swimmer"))
			return new NarjilloView((Narjillo) thing);
		else if (thing.getLabel().equals("food")) {
			return new FoodView((Food)thing);
		} else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}
}
