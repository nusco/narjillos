package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.shared.things.Thing;

public interface EcosystemEventListener {

	void thingAdded(Thing thing);
	void thingRemoved(Thing thing);
}
