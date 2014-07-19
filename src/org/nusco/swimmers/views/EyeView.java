package org.nusco.swimmers.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.nusco.swimmers.creature.Swimmer;

class EyeView extends ThingView {

	private final Circle circle = createCircle();

	public EyeView(Swimmer swimmer) {
		super(swimmer);
	}

	@Override
	public Node toNode() {
		double energy = ((Swimmer)getThing()).getEnergy();
		circle.setFill(toColor(energy));
		return circle;
	}

	private Color toColor(double energy) {
		double intensity = Math.min(0.8, energy / 50_000);
		return new Color(0, intensity, 0, 0.8);
	}

	private Circle createCircle() {
		Circle result = new Circle(6);
		return result;
	}
}
