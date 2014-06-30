package org.nusco.swimmers.creature.body.pns;

import java.util.LinkedList;

import org.nusco.swimmers.shared.physics.Vector;

/**
 * Outputs the same signal it received as an input, but
 * delayed by a given number of ticks.
 */
public class DelayNerve implements Nerve {

	private final int delay;
	private final LinkedList<Vector> buffer = new LinkedList<>();

	public DelayNerve(int delay) {
		this.delay = delay;
	}

	@Override
	public Vector tick(Vector inputSignal) {
		buffer.add(inputSignal);
		if(buffer.size() < delay)
			return buffer.getFirst();
		return buffer.pop();
	}
}
