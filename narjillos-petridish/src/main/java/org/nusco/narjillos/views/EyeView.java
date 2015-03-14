package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Narjillo;

class EyeView extends RoundObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.12;

	private final Narjillo narjillo;

	public EyeView(Narjillo narjillo) {
		super(6);
		this.narjillo = narjillo;
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		getShape().setFill(toColor(zoomLevel));
		moveTo(narjillo.getPosition());
		return getShape();
	}

	private Color toColor(double zoomLevel) {
		return new Color(0, getIntensity(), 0, getTransparency(zoomLevel));
	}

	private double getIntensity() {
		return Math.max(0, Math.min(0.8, narjillo.getEnergyLevel()));
	}
	
	protected double clipToRange(double result, double min, double max) {
		return Math.max(min, Math.min(max, result));
	}

	private double getTransparency(double zoomLevel) {
		return clipToRange((zoomLevel - MINIMUM_ZOOM_LEVEL) * 4, 0, 0.8);
	}
}
