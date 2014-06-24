package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.PassNerve;
import org.nusco.swimmers.physics.Vector;


class NullOrgan extends Organ {

	public NullOrgan(Organ parent) {
		super(0, 0, 0, 0, new PassNerve(), parent);
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
	public int getRGB() {
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
