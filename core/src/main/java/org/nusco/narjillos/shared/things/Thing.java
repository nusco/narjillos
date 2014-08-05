package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Vector;

/**
 * Something that has a physical representation in the environment.
 */
public interface Thing {

	Vector getPosition();
	void tick();
	String getLabel();
}
