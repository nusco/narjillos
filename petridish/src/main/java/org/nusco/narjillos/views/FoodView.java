package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.nusco.narjillos.shared.things.FoodPiece;

class FoodView extends RoundObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.035;

	public FoodView(FoodPiece food) {
		super(food, FoodPiece.RADIUS);
	}

	public Node toNode(double zoomLevel, boolean infraredOn, boolean effectsOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;

		getShape().setFill(getColor(infraredOn));
		
		if (infraredOn) {
			getShape().setStroke(Color.WHITE);
			getShape().setStrokeWidth(3);
		} else {
			getShape().setStrokeWidth(0);
		}

		moveToPosition();
		
		if (effectsOn)
			getShape().setEffect(getEffects(zoomLevel, infraredOn));
		
		return getShape();
	}

	private Color getColor(boolean infraredOn) {
		if (infraredOn)
			return Color.RED;
		return Color.BROWN;
	}
}