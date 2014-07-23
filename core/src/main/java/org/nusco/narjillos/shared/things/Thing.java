package org.nusco.narjillos.shared.things;

import org.nusco.narjillos.shared.physics.Vector;

public interface Thing {

	Vector getPosition();
	void setPosition(Vector position);
	void tick();
	String getLabel();
}
