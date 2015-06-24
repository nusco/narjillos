package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.core.things.Thing;

public interface CultureEventListener {

	void added(Thing thing);

	void removed(Thing thing);
}
