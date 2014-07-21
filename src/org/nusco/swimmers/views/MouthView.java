package org.nusco.swimmers.views;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import org.nusco.swimmers.creature.Narjillo;

class MouthView extends ThingView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.13;

	private static final int LENGTH = 50;
	private final Group group = new Group();
	private final Line line1 = createLine();
	private final Line line2 = createLine();

	public MouthView(Narjillo swimmer) {
		super(swimmer);
		group.getChildren().add(line1);
		group.getChildren().add(line2);
	}

	@Override
	public Node toNode(double zoomLevel) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;
		
		Color color = new Color(0, 0.8, 0, getTransparency(zoomLevel));
		line1.setStroke(color);
		line2.setStroke(color);

		rotate(line1, 10);
		rotate(line2, -10);
		return group;
	}

	private double getTransparency(double zoomLevel) {
		double result = (zoomLevel - MINIMUM_ZOOM_LEVEL) * 6;
		
		if (result < 0)
			return 0;
		
		if (result > 1)
			return 1;
		
		return result;
	}
	
	private void rotate(Line line, int angle) {
		line.getTransforms().clear();
		line.getTransforms().add(new Rotate(getNarjillo().getTargetDirection().getAngle() + angle));
	}

	private Line createLine() {
		Line result = new Line(0, 0, LENGTH, 2);
		return result;
	}

	private Narjillo getNarjillo() {
		return (Narjillo)getThing();
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getNarjillo().getPosition(), LENGTH);
	}
}
