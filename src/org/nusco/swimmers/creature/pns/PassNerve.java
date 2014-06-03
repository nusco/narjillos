package org.nusco.swimmers.creature.pns;

import org.nusco.swimmers.physics.Vector;

public class PassNerve extends Nerve {

	@Override
	public Vector send(Vector inputSignal) {
		return inputSignal;
	}
}
