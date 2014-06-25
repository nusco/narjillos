package org.nusco.swimmers.graphics;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.physics.Vector;

public class VectorView {

	private final Vector vector;
	private final Vector position;

	public VectorView(Vector vector, Vector position) {
		this.vector = vector;
		this.position = position;
	}

	public Node toShape() {
		Group result = new Group();
		
		Line line1 = createLine();
		line1.getTransforms().add(new Rotate(vector.getAngle() + 10));
		result.getChildren().add(line1);
		
		Line line2 = createLine();
		line2.getTransforms().add(new Rotate(vector.getAngle() - 10));
		result.getChildren().add(line2);
		
		return result;
	}

	private Line createLine() {
		Line line1 = new Line(0, 0, 50, 2);
		line1.setStroke(Color.GREEN);

		// shift towards the center of the screen
		line1.getTransforms().add(new Translate(SwimmerView.OFFSET_X + position.getX(), SwimmerView.OFFSET_Y + position.getY()));
		return line1;
	}
}
