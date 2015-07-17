package org.nusco.narjillos.experiment.environment;

import org.nusco.narjillos.core.things.Thing;

public interface EnvironmentEventListener {

	void added(Thing thing);

	void removed(Thing thing);
}
