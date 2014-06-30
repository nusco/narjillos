package org.nusco.swimmers.graphics;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.swimmers.pond.Food;
import org.nusco.swimmers.shared.physics.Vector;

class FoodView extends ThingView {

	private final Food food;

	public FoodView(Food food) {
		this.food = food;
	}

	public Node toNode() {
			return toCircle();
	}

	private Node toCircle() {
		Circle result = new Circle(10);

		Color baseColor = Color.LIMEGREEN;
		result.setFill(new Color(baseColor.getBlue(), baseColor.getRed(), baseColor.getGreen(), 0.8));

		result.getTransforms().add(moveToStartPoint());

		return result;
	}

	private Translate moveToStartPoint() {
		Vector position = food.getPosition();
		return new Translate(position.getX(), position.getY());
	}
}