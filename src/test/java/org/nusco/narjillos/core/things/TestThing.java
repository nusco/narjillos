package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

class TestThing implements Thing {

	private final Vector position;

	public TestThing(Vector position) {
		this.position = position;
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
		return "thing";
	}

	@Override
	public Thing getInteractor() {
		return Thing.NULL;
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
	public boolean isDead() {
		return false;
	}

	@Override
	public double getRadius() {
		return 0;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return BoundingBox.punctiform(getPosition());
	}

	@Override
	public String toString() {
		return getPosition().toString();
	}
}