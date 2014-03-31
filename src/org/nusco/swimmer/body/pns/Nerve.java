package org.nusco.swimmer.body.pns;

import java.util.LinkedList;
import java.util.List;

public abstract class Nerve {

	private final List<Nerve> children = new LinkedList<>();
	private double outputSignal = 1;

	public abstract double process(double inputSignal);

	public void send(double inputSignal) {
		outputSignal = process(inputSignal);
		for (Nerve neuron : children)
			neuron.send(outputSignal);
	}

	public double readOutputSignal() {
		return outputSignal;
	}

	public void connectTo(Nerve child) {
		children.add(child);
	}
}
