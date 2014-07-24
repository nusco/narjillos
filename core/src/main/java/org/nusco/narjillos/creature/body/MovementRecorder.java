package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.shared.physics.Segment;

public interface MovementRecorder {

	MovementRecorder NULL = new MovementRecorder() {
		@Override
		public void record(Segment beforeMovement, Organ organ) {}
	};

	public void record(Segment beforeMovement, Organ organ);
}
