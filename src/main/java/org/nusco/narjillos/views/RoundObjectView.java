package org.nusco.narjillos.views;

import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.utilities.Viewport;

abstract class RoundObjectView implements ItemView {

	private final Circle shape;
	private final double radius;
	private Vector position = Vector.ZERO;

	public RoundObjectView(double radius) {
		this.shape = new Circle(radius);
		this.radius = radius;
	}
	
	protected final Circle getShape() {
		return shape;
	}

	protected void moveTo(Vector position) {
		this.position = position;
		getShape().getTransforms().clear();
		getShape().getTransforms().add(new Translate(position.x, position.y));
	}

	@Override
	public boolean isVisible(Viewport viewport) {
		return viewport.isVisible(position, getRadius());
	}

	protected final double getRadius() {
		return radius;
	}
}