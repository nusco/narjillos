package org.nusco.swimmer.body.pns;

import java.util.LinkedList;

class DelayNerve extends Nerve {

	private final int delay;
	private final LinkedList<Double> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	public double process(double inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay)
			return 1.0;
		return buffer.pop();
	}
}
