package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.nusco.narjillos.creature.Narjillo;

class EyeView extends CircularObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.12;
	private final Circle circle;

	public EyeView(Narjillo swimmer) {
		super(swimmer);
		circle = createCircle();
	}

	@Override
	public Node toNode(double zoomLevel) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		double energy = ((Narjillo)getThing()).getEnergy();
		circle.setFill(toColor(energy, zoomLevel));
		return circle;
	}

	private Color toColor(double energy, double zoomLevel) {
		double intensity = Math.min(0.8, energy / Narjillo.MAX_ENERGY);
		return new Color(0, intensity, 0, getTransparency(zoomLevel));
	}

	private double getTransparency(double zoomLevel) {
		double result = (zoomLevel - MINIMUM_ZOOM_LEVEL) * 4;
		
		if (result < 0)
			return 0;

		final double maxAlpha = 0.8;
		if (result > maxAlpha)
			return maxAlpha;
		
		return result;
	}

	private Circle createCircle() {
		return new Circle(getRadius());
	}

	@Override
	protected final double getRadius() {
		return 6;
	}
}
