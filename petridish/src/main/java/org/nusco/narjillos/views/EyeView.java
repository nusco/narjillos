package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Narjillo;

class EyeView extends RoundObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.12;

	public EyeView(Narjillo narjillo) {
		super(narjillo, 6);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		getShape().setFill(toColor(zoomLevel));
		moveToPosition();
		return getShape();
	}

	private Color toColor(double zoomLevel) {
		return new Color(0, getIntensity(), 0, getTransparency(zoomLevel));
	}

	private double getIntensity() {
		return clipToRange(((Narjillo) getThing()).getEnergyPercent(), 0, 0.8);
	}

	private double getTransparency(double zoomLevel) {
		return clipToRange((zoomLevel - MINIMUM_ZOOM_LEVEL) * 4, 0, 0.8);
	}
}
