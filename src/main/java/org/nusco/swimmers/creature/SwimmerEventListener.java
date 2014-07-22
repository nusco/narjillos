package org.nusco.swimmers.creature;

import org.nusco.swimmers.shared.physics.Segment;

public interface SwimmerEventListener {

	void moved(Segment movement);
	void died();
}
