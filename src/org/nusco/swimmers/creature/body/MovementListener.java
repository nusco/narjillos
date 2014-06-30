package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.physics.Segment;

public interface MovementListener {

	MovementListener NULL = new MovementListener() {
		@Override
		public void moveEvent(Segment beforeMovement, Segment afterMovement) {}
	};

	public void moveEvent(Segment beforeMovement, Segment afterMovement);
}
