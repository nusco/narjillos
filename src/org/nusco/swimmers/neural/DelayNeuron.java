package org.nusco.swimmers.neural;

import java.util.LinkedList;

public class DelayNeuron extends Neuron {

	private final int delay;
	private final LinkedList<Double> buffer = new LinkedList<>();

	public DelayNeuron(int delay) {
		this.delay = delay;
	}

	public double process(double inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() >= delay)
			return buffer.pop();
		else
			return 1.0;
	}
}
