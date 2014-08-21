package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.Vector;

class EyeView extends RoundObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.12;
	private final Circle circle;

	public EyeView(Narjillo narjillo) {
		super(narjillo, 6);
		circle = new Circle(getRadius());
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		circle.setFill(toColor(zoomLevel));

		Vector position = getThing().getPosition();
		circle.getTransforms().clear();
		circle.getTransforms().add(new Translate(position.x, position.y));

		return circle;
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
