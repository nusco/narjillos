package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.physics.Vector;

public interface MovementListener {

	MovementListener NULL = new MovementListener() {
		@Override
		public void moveEvent(Vector beforeVector, Vector beforeStartPoint, Vector afterVector, Vector afterStartPoint) {}
	};

	public void moveEvent(Vector beforeVector, Vector beforeStartPoint, Vector afterVector, Vector afterStartPoint);
}
