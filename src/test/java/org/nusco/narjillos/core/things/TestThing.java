package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

class TestThing implements Thing {

	private final String label;

	private final Vector position;

	public TestThing(Vector position, Integer id) {
		this.position = position;
		this.label = id.toString();
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public Vector getCenter() {
		return getPosition();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Thing getLastInteractingThing() {
		return Thing.NULL;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	public Segment tick() {
		return null;
	}

	@Override
	public Energy getEnergy() {
		return null;
	}

	@Override
	public double getRadius() {
		return 0;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return BoundingBox.punctiform(getPosition());
	}
}