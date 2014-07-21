package org.nusco.swimmers.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.nusco.swimmers.creature.Narjillo;

class EyeView extends CircularObjectView {

	private final Circle circle;

	public EyeView(Narjillo swimmer) {
		super(swimmer);
		circle = createCircle();
	}

	@Override
	public Node toNode() {
		double energy = ((Narjillo)getThing()).getEnergy();
		circle.setFill(toColor(energy));
		return circle;
	}

	private Color toColor(double energy) {
		double intensity = Math.min(0.8, energy / Narjillo.MAX_ENERGY);
		return new Color(0, intensity, 0, 0.8);
	}

	private Circle createCircle() {
		return new Circle(getRadius());
	}

	@Override
	protected final double getRadius() {
		return 6;
	}
}
