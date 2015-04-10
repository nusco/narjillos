package org.nusco.narjillos.application.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.core.physics.Vector;
import org.nusco.narjillos.creature.Narjillo;

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
		Vector position = narjillo.getCenterOfMass();
		moveTo(position);
		return getShape();
	}
}
