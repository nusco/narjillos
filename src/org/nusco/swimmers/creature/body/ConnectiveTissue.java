package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;

/**
 * An Organ so small, it cannot be seen.
 */
class ConnectiveTissue extends Organ {

	public ConnectiveTissue(Organ parent) {
		this(parent, new PassNerve());
	}

	protected ConnectiveTissue(Organ parent, Nerve nerve) {
		super(0, 0, 0, 0, nerve, parent);
	}
	
	@Override
	public Vector getStartPoint() {
		return getParent().getEndPoint();
	}

	@Override
	public Vector getEndPoint() {
		return getParent().getEndPoint();
	}

	@Override
	public int getColor() {
		return 0;
	}

	@Override
	public int getLength() {
		return 0;
	}

	@Override
	public int getThickness() {
		return 0;
	}

	@Override
	public double getAngle() {
		return 0;
	}

	public double getRelativeAngle() {
		return getParent().getRelativeAngle();
	}

	@Override
	public String toString() {
		return "<null organ>";
	}

	@Override
	protected void move(Vector signal) {
	}
}
