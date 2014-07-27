package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.pond.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;

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
		
		return toNode(viewport.getZoomLevel());
	}
	
	protected abstract boolean isVisible(Viewport viewport);

	public abstract Node toNode(double zoomLevel);
	
	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			return new NarjilloView((Narjillo) thing);
		else if (thing.getLabel().equals("food_piece")) {
			return new FoodView((FoodPiece)thing);
		} else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}

	protected Effect getHaloEffect(double zoomLevel) {
		double minZoomLevel = 0.25;
		if (zoomLevel <= minZoomLevel)
			return null;
		double alpha = Math.min((zoomLevel - minZoomLevel) * 3, 1);
		Color color = new Color(0.9, 0.9, 0.9, alpha);
		return new DropShadow(20, 7, 7, color);
	}
}
