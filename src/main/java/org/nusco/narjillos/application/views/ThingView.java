package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;

import org.nusco.narjillos.application.utilities.Viewport;
import org.nusco.narjillos.core.things.FoodPellet;
import org.nusco.narjillos.core.things.Thing;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;

abstract class ThingView implements ItemView {

	private final Thing thing;

	ThingView(Thing thing) {
		this.thing = thing;
	}

	public Thing getThing() {
		return thing;
	}

	public final Node toNode(Viewport viewport, boolean infraredOn, boolean effectsOn) {
		if (!isVisible(viewport))
			return null;

		return toNode(viewport.getZoomLevel(), infraredOn, effectsOn);
	}

	Effect getEffects(double zoomLevel, boolean infraredOn) {
		if (infraredOn)
			return getHaloEffect(zoomLevel * 1.5);

		return getHaloEffect(zoomLevel);
	}

	static ThingView createViewFor(Thing thing) {
		if (thing.getLabel().equals("narjillo"))
			return new NarjilloView((Narjillo) thing);
		else if (thing.getLabel().equals("food_pellet"))
			return new FoodView((FoodPellet) thing);
		else if (thing.getLabel().equals("egg"))
			return new EggView((Egg) thing);
		else
			throw new RuntimeException("Unknown thing: " + thing.getLabel());
	}

	private Effect getHaloEffect(double zoomLevel) {
		double minZoomLevel = 0.2;
		if (zoomLevel <= minZoomLevel)
			return null;
		double alpha = (zoomLevel - minZoomLevel) * 2.5;
		double limitedAlpha = Math.max(0, Math.min(1, alpha));
		Color color = new Color(0.9, 0.9, 0.9, limitedAlpha);
		return new DropShadow(20, 7, 7, color);
	}
}
