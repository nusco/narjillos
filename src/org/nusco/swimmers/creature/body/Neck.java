package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.WaveNerve;

/**
 * A special case of ConnectiveTissue that generates wave signals.
 */
class Neck extends ConnectiveTissue {
	
	private static final double WAVE_SIGNAL_FREQUENCY = 0.005;

	public Neck(Head parent, double metabolicRate) {
		super(parent, new WaveNerve(metabolicRate * WAVE_SIGNAL_FREQUENCY));
	}
}
