package org.nusco.swimmer.body.pns;

import java.util.LinkedList;

import org.nusco.swimmer.physics.Vector;

class DelayNerve extends Nerve {

	private final int delay;
	private final LinkedList<Vector> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	public Vector process(Vector inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay)
			return Vector.ONE;
		return buffer.pop();
	}
}
