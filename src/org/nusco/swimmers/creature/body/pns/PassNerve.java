package org.nusco.swimmers.creature.body.pns;

import org.nusco.swimmers.physics.Vector;

public class PassNerve implements Nerve {

	@Override
	public Vector send(Vector inputSignal) {
		return inputSignal;
	}
}
