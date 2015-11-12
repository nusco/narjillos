package org.nusco.narjillos.core.things;

import org.nusco.narjillos.core.geometry.Segment;
import org.nusco.narjillos.core.geometry.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public interface Thing {

	public static Thing NULL = new Thing() {
		
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
	};
	
	/**
	 * @return The movement segment.
	 */
	public Segment tick();
	public Vector getPosition();
	public Vector getCenter();
	public double getRadius();
	public Energy getEnergy();
	public String getLabel();
}
