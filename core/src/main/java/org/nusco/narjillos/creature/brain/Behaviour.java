package org.nusco.narjillos.creature.brain;

import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;

public abstract class Behaviour {

	private final String name;

	public Behaviour(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public Vector lookAt(Pond pond, Vector self) {
		Vector target = acquireTarget(pond, self);
		return target.minus(self).normalize(1);
	}

	public abstract Vector acquireTarget(Pond pond, Vector self);
}
