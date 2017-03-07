package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public interface Thing {

	Thing NULL = new Thing() {

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
			return new LifeFormEnergy(0, 0);
		}

		@Override
		public String getLabel() {
			return "null_thing";
		}

		@Override
		public Thing getLastInteractingThing() {
			return NULL;
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

	String getLabel();

	Thing getLastInteractingThing();
}
