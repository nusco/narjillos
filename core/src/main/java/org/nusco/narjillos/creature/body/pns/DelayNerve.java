package org.nusco.narjillos.creature.body.pns;

import java.util.LinkedList;

/**
 * Outputs the same signal it receives as an input, delayed by a given number of ticks.
 */
public strictfp class DelayNerve implements Nerve {

	private final int delay;
	private final LinkedList<Double> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	@Override
	public double tick(double inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay + 1)
			return 0;
		return buffer.pop();
	}

	public LinkedList<Double> getBuffer() {
		return buffer;
	}

	public int getDelay() {
		return delay;
	}
}
