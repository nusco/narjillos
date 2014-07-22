package org.nusco.swimmers.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.pond.FoodPiece;
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
		
		return toNode(viewport.getZoomLevel());
	}
	
	protected abstract boolean isVisible(Viewport viewport);

	public abstract Node toNode(double zoomLevel);
	
	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("swimmer"))
			return new NarjilloView((Narjillo) thing);
		else if (thing.getLabel().equals("food_piece")) {
			return new FoodView((FoodPiece)thing);
		} else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}

	protected DropShadow getShadow(double zoomLevel) {
		if (zoomLevel <= 0.5)
			return null;
		double alpha = Math.min((zoomLevel - 0.5) * 2, 1);
		Color color = new Color(0.1, 0.1, 0.1, alpha );
		return new DropShadow(12, 3, 3, color);
	}
}
