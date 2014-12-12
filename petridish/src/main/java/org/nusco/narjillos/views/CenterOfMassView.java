package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * Only used for debugging.
 */
class CenterOfMassView extends RoundObjectView {

	public CenterOfMassView(Narjillo narjillo) {
		super(narjillo, 6);
		getShape().setFill(Color.BLACK);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean motionBlurOn) {
		Vector position = ((Narjillo)getThing()).calculateCenterOfMass();
		moveTo(position);
		return getShape();
	}
}
