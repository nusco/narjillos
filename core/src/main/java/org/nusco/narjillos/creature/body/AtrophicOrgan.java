package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.creature.body.pns.PassNerve;
import org.nusco.narjillos.shared.physics.Vector;

/**
 * An Organ that is invisible to the naked eye---but still contains a Nerve.
 */
class AtrophicOrgan extends BodyPart {

	public AtrophicOrgan(Organ parent) {
		this(parent, new PassNerve());
	}

	protected AtrophicOrgan(Organ parent, Nerve nerve) {
		super(0, 0, parent.getColor(), parent, nerve);
	}

	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle();
	}

	// Optimization: ticking is simpler for ConnectiveTissue.
	// The duplication is ugly, but it does help performance.
	@Override
	public Vector tick(Vector inputSignal, MovementRecorder movementListener) {
		resetAllCaches();
		Vector outputSignal = getNerve().tick(inputSignal);
		tickChildren(outputSignal, movementListener);
		return outputSignal;
	}

	@Override
	protected void move(Vector signal) {
	}
}
