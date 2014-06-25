package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.WaveNerve;

public class Neck extends ConnectiveTissue {
	
	private static final double WAVE_SIGNAL_FREQUENCY = 0.01;

	public Neck(Head parent) {
		super(parent, new WaveNerve(WAVE_SIGNAL_FREQUENCY));
	}

	public Head getHead() {
		return (Head)parent;
	}
}
