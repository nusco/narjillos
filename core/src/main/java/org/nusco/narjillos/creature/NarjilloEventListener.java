package org.nusco.narjillos.creature;

import org.nusco.narjillos.shared.physics.Segment;

public interface NarjilloEventListener {

	void moved(Segment movement);
	void died();
}
