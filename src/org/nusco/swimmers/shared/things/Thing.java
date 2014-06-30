package org.nusco.swimmers.shared.things;

import org.nusco.swimmers.shared.physics.Vector;

public interface Thing {

	void setPosition(Vector position);
	Vector getPosition();
	void tick();
	String getLabel();
}
