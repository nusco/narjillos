package org.nusco.swimmers.pond;

import org.nusco.swimmers.shared.things.Thing;

public interface PondEventListener {

	void thingAdded(Thing thing);
	void thingRemoved(Thing thing);
}
