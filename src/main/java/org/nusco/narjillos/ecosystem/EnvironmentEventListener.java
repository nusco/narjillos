package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.core.things.Thing;

public interface EnvironmentEventListener {

	void thingAdded(Thing thing);

	void thingRemoved(Thing thing);
}
