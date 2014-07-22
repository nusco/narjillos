package org.nusco.swimmers.shared.things;

import org.nusco.swimmers.shared.physics.Vector;

public interface Thing {

	Vector getPosition();
	void setPosition(Vector position);
	void tick();
	String getLabel();
}
