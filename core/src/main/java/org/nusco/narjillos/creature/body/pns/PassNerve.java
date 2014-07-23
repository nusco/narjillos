package org.nusco.narjillos.creature.body.pns;

import org.nusco.narjillos.shared.physics.Vector;

/**
 * Always outputs the same vector it receives in input.
 */
public class PassNerve implements Nerve {

	@Override
	public Vector tick(Vector inputSignal) {
		return inputSignal;
	}
}
