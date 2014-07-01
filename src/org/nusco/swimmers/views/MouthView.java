package org.nusco.swimmers.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import org.nusco.swimmers.creature.Swimmer;

class MouthView extends ThingView {

	private final Swimmer swimmer;
	private final Group group = new Group();
	private final Line line1 = createLine();
	private final Line line2 = createLine();

	public MouthView(Swimmer swimmer) {
		this.swimmer = swimmer;
		group.getChildren().add(line1);
		group.getChildren().add(line2);
	}

	@Override
	public Node toNode() {
		rotate(line1, 10);
		rotate(line2, -10);
		return group;
	}

	private void rotate(Line line, int angle) {
		line.getTransforms().clear();
		line.getTransforms().add(new Rotate(swimmer.getCurrentTarget().getAngle() + angle));
	}

	private Line createLine() {
		Line result = new Line(0, 0, 50, 2);
		result.setStroke(Color.GREEN);
		return result;
	}
}
