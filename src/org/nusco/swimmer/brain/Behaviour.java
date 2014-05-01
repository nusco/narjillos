package org.nusco.swimmer.brain;

import org.nusco.swimmer.physics.Vector;
import org.nusco.swimmer.pond.Pond;

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
		return target.minus(self).normalize();
	}

	public abstract Vector acquireTarget(Pond pond, Vector self);
}
