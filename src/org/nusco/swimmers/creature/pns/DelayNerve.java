package org.nusco.swimmers.creature.pns;

import java.util.LinkedList;

import org.nusco.swimmers.physics.Vector;

public class DelayNerve extends Nerve {

	private final int delay;
	private final LinkedList<Vector> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	@Override
	public Vector send(Vector inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay)
			return buffer.getFirst();
		return buffer.pop();
	}
}
