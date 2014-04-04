package org.nusco.swimmer.body.pns;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmer.physics.Vector;

public abstract class Nerve {

	private final List<Nerve> children = new LinkedList<>();
	private Vector outputSignal = Vector.ONE;

	public abstract Vector process(Vector inputSignal);

	public void send(Vector inputSignal) {
		outputSignal = process(inputSignal);
		for (Nerve child : children)
			child.send(outputSignal);
	}

	public Vector readOutputSignal() {
		return outputSignal;
	}

	public void connectTo(Nerve child) {
		children.add(child);
	}
}
