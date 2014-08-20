package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * Only used for debugging.
 */
class CenterOfMassView extends RoundObjectView {

	private final Circle circle;

	public CenterOfMassView(Narjillo narjillo) {
		super(narjillo, 6);
		circle = new Circle(getRadius());
		circle.setFill(Color.BLACK);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn) {
		Vector position = ((Narjillo)getThing()).getCenterOfMass();
		circle.getTransforms().clear();
		circle.getTransforms().add(new Translate(position.x, position.y));
		return circle;
	}
}
