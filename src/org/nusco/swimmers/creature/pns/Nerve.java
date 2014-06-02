package org.nusco.swimmers.creature.pns;

import org.nusco.swimmers.physics.Vector;

public abstract class Nerve {

	private Vector outputSignal = Vector.ZERO_ONE;

	public abstract Vector process(Vector inputSignal);

	public void send(Vector inputSignal) {
		outputSignal = process(inputSignal);
	}

	public Vector getOutputSignal() {
		return outputSignal;
	}
}
