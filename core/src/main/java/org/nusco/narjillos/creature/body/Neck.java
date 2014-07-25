package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.WaveNerve;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.ColorByte;

/**
 * A special case of ConnectiveTissue that generates wave signals.
 */
class Neck extends BodyPart {
	
	private static final double WAVE_SIGNAL_FREQUENCY = 0.005;

	public Neck(Head parent, double metabolicRate) {
		super(0, 0, new ColorByte(0), parent, new WaveNerve(metabolicRate * WAVE_SIGNAL_FREQUENCY));
	}

	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle();
	}

	@Override
	protected void move(Vector signal) {
	}
}
