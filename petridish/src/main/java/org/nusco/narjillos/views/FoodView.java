package org.nusco.narjillos.views;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.pond.FoodPiece;
import org.nusco.narjillos.shared.physics.Vector;

class FoodView extends CircularObjectView {

	private final Node circle;

	public FoodView(FoodPiece food) {
		super(food);
		circle = createCircle(food);
	}

	public Node toNode(double zoomLevel) {
		circle.getTransforms().clear();
		circle.getTransforms().add(moveToStartPoint());
		circle.setEffect(getHaloEffect(zoomLevel));
		return circle;
	}

	private Node createCircle(FoodPiece food) {
		Circle result = new Circle(getRadius());
		Color baseColor = Color.PURPLE;
		result.setFill(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 0.8));
		return result;
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