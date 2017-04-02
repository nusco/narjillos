package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.Bounded;
import org.nusco.narjillos.core.geometry.BoundingBox;
import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public interface Thing extends Bounded {

	Thing NULL = new Thing() {

		@Override
		public BoundingBox getBoundingBox() {
			return BoundingBox.punctiform(Vector.ZERO);
		}

		@Override
		public Segment tick() {
			return new Segment(Vector.ZERO, Vector.ZERO);
		}

		@Override
		public Vector getPosition() {
			return Vector.ZERO;
		}

		@Override
		public Vector getCenter() {
			return Vector.ZERO;
		}

		@Override
		public double getRadius() {
			return 0;
		}

		@Override
		public Energy getEnergy() {
			return Energy.INFINITE;
		}

		@Override
		public boolean isDead() {
			return false;
		}

		@Override
		public String getLabel() {
			return "null_thing";
		}

		@Override
		public Thing getInteractor() {
			return this;
		}
	};

	/**
	 * @return The movement segment.
	 */
	Segment tick();

	Vector getPosition();

	Vector getCenter();

	double getRadius();

	Energy getEnergy();

	boolean isDead();

	String getLabel();

	Thing getInteractor();
}
