package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.things.FoodPiece;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.views.utilities.Viewport;

abstract class ThingView {

	private final Thing thing;

	public ThingView(Thing thing) {
		this.thing = thing;
	}

	public Thing getThing() {
		return thing;
	}

	public final Node toNode(Viewport viewport, boolean infraredOn) {
		if (!isVisible(viewport))
			return null;
		
		return toNode(viewport.getZoomLevel(), infraredOn);
	}
	
	protected abstract boolean isVisible(Viewport viewport);

	public abstract Node toNode(double zoomLevel, boolean infraredOn);
	
	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			return new NarjilloView((Narjillo) thing);
		else if (thing.getLabel().equals("food_piece"))
			return new FoodView((FoodPiece)thing);
		else if (thing.getLabel().equals("egg"))
			return new EggView((Egg)thing);
		else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}

	protected Effect getEffects(double zoomLevel, boolean infraredOn) {
		if (infraredOn)
			return getHaloEffect(zoomLevel * 1.5);
		
		return getHaloEffect(zoomLevel);
	}

	private Effect getHaloEffect(double zoomLevel) {
		double minZoomLevel = 0.2;
		if (zoomLevel <= minZoomLevel)
			return null;
		double alpha = clipToRange((zoomLevel - minZoomLevel) * 2.5, 0, 1);
		Color color = new Color(0.9, 0.9, 0.9, alpha);
		return new DropShadow(20, 7, 7, color);
	}
	
	protected double clipToRange(double result, double min, double max) {
		return Math.max(min, Math.min(max, result));
	}
}
