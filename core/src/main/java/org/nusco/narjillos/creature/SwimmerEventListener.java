package org.nusco.narjillos.creature;

import org.nusco.narjillos.shared.physics.Segment;

public interface SwimmerEventListener {

	void moved(Segment movement);
	void died();
}
