package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * Only used for debugging.
 */
class CenterOfMassView extends RoundObjectView {

	private final Narjillo narjillo;

	public CenterOfMassView(Narjillo narjillo) {
		super(6);
		this.narjillo = narjillo;
		getShape().setFill(Color.BLACK);
	}

	@Override
	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		Vector position = (narjillo.calculateCenterOfMass());
		moveTo(position);
		return getShape();
	}
}
