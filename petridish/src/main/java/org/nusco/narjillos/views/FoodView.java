package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.pond.FoodPiece;
import org.nusco.narjillos.shared.physics.Vector;

class FoodView extends CircularObjectView {

	private static final double MINIMUM_ZOOM_LEVEL = 0.035;

	private final Shape circle;

	private Color color;

	public FoodView(FoodPiece food) {
		super(food);

		Color baseColor = Color.BLUE;
		color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 0.8);
		
		circle = new Circle(getRadius());
	}

	public Node toNode(double zoomLevel, boolean infraredOn) {
		if (zoomLevel < MINIMUM_ZOOM_LEVEL)
			return null;

		if (infraredOn)
			circle.setFill(Color.RED);
		else
			circle.setFill(color);
		
		circle.getTransforms().clear();
		circle.getTransforms().add(moveToStartPoint());
		circle.setEffect(getEffects(zoomLevel, infraredOn));
		return circle;
	}

	private Translate moveToStartPoint() {
		Vector position = getThing().getPosition();
		return new Translate(position.x, position.y);
	}

	@Override
	protected final double getRadius() {
		return 7;
	}
}