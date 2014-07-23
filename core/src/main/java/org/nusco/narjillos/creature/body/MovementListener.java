package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;

public interface MovementListener {

	MovementListener NULL = new MovementListener() {
		@Override
		public void moveEvent(Segment beforeMovement, Organ organ) {}
	};

	public void moveEvent(Segment beforeMovement, Organ organ);
}
