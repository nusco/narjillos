package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.physics.Vector;

public interface MovementListener {

	MovementListener NULL = new MovementListener() {
		@Override
		public void moveEvent(Vector before, Vector after) {}
	};

	public void moveEvent(Vector before, Vector after);
}
