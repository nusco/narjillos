package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Egg;

class EggView extends RoundObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.02;
	private static final int RADIUS = 25;

	public EggView(Egg egg) {
		super(egg, RADIUS);
	}

	public Node toNode(double zoomLevel, boolean infraredOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;

		getShape().setRadius(Math.min(getEgg().getAge(), RADIUS));
		getShape().setFill(getFillColor(infraredOn));

		getShape().setEffect(getEffects(zoomLevel, infraredOn));
		moveToPosition();
		
		return getShape();
	}

	private Color getFillColor(boolean infraredOn) {
		Color color = (infraredOn ? Color.RED : Color.BURLYWOOD);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), getAlpha());
	}

	private double getAlpha() {
		return 1 - getEgg().getDecay();
	}

	private Egg getEgg() {
		return (Egg) getThing();
	}
}