package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

/**
 * An Organ that is invisible to the naked eye---but still contains a Nerve.
 */
class ConnectiveTissue extends Organ {

	public ConnectiveTissue(Organ parent) {
		this(parent, new PassNerve());
	}

	protected ConnectiveTissue(Organ parent, Nerve nerve) {
		super(0, 0, 0, nerve, parent);
	}

	@Override
	public double getAbsoluteAngle() {
		return getParent().getAbsoluteAngle();
	}

	@Override
	public int getColor() {
		return getParent().getColor();
	}

	@Override
	public String toString() {
		return "<null organ>";
	}

	@Override
	protected void move(Vector signal) {
	}
}
