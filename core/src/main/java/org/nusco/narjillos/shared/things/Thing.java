package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public interface Thing {

	Thing NULL = new Thing() {
		
		@Override
		public void tick() {
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
	
	void tick();
	Vector getPosition();
	Energy getEnergy();
	String getLabel();
}
