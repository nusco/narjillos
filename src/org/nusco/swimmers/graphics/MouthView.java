package org.nusco.swimmers.graphics;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import org.nusco.swimmers.shared.physics.Vector;

public class MouthView extends ThingView {

	private final Vector direction;

	public MouthView(Vector direction) {
		this.direction = direction;
	}

	@Override
	public Node toNode() {
		Group result = new Group();
		
		Line line1 = createLine();
		line1.getTransforms().add(new Rotate(direction.getAngle() + 10));
		result.getChildren().add(line1);
		
		Line line2 = createLine();
		line2.getTransforms().add(new Rotate(direction.getAngle() - 10));
		result.getChildren().add(line2);
		
		return result;
	}

	private Line createLine() {
		Line result = new Line(0, 0, 50, 2);
		result.setStroke(Color.GREEN);
		return result;
	}
}
