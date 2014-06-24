package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.physics.Vector;

public interface MovementListener {

	public void moveEvent(Vector before, Vector after);
}
