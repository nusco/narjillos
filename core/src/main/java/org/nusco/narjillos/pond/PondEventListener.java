package org.nusco.narjillos.pond;

import org.nusco.narjillos.shared.things.Thing;

public interface PondEventListener {

	void thingAdded(Thing thing);
	void thingRemoved(Thing thing);
}
