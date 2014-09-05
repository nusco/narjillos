package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Segment;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public strictfp interface Thing {

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
		public Energy getEnergy() {
			return new Energy(0, 0);
		}
		
		@Override
		public String getLabel() {
			return "null_thing";
		}
	};
	
	/**
	 * @return The movement segment.
	 */
	Segment tick();

	Vector getPosition();
	Energy getEnergy();
	String getLabel();
}
