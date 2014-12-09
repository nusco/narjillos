package org.nusco.narjillos.views;

import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Thing;
import org.nusco.narjillos.views.utilities.Viewport;

public abstract class RoundObjectView extends ThingView {

	private final Circle shape;
	private final int radius;

	public RoundObjectView(Thing thing, int radius) {
		super(thing);
		this.shape = new Circle(radius);
		this.radius = radius;
	}
	
	protected final Circle getShape() {
		return shape;
	}

	protected void moveToPosition() {
		Vector position = getThing().getPosition();
		moveTo(position);
	}

	protected void moveTo(Vector position) {
		getShape().getTransforms().clear();
		getShape().getTransforms().add(new Translate(position.x, position.y));
	}

	@Override
	protected boolean isVisible(Viewport viewport) {
		return viewport.isVisible(getThing().getPosition(), getRadius());
	}

	protected final double getRadius() {
		return radius;
	}
}