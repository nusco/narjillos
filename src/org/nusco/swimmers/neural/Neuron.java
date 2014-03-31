package org.nusco.swimmers.neural;

import java.util.LinkedList;
import java.util.List;

public abstract class Neuron {

	private final List<Neuron> children = new LinkedList<>();
	private double outputSignal = 1;

	public abstract double process(double inputSignal);

	public void send(double inputSignal) {
		outputSignal = process(inputSignal);
		for (Neuron neuron : children)
			neuron.send(outputSignal);
	}

	public double readOutputSignal() {
		return outputSignal;
	}

	public void connectTo(Neuron child) {
		children.add(child);
	}
}
