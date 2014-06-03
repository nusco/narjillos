package org.nusco.swimmers.graphics;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.physics.Vector;

public class VectorView {

	private final Vector vector;

	public VectorView(Vector vector) {
		this.vector = vector;
	}

	public Node toShape() {
		Rectangle result = new Rectangle(0, 0, 50, 2);
		
		result.setFill(Color.DARKGREEN);

		// shift towards the center of the screen
		result.getTransforms().add(new Translate(SwimmerView.OFFSET_X, SwimmerView.OFFSET_Y));

		// rotate in position
		result.getTransforms().add(new Rotate(vector.getAngle()));

		return result;
	}
}
