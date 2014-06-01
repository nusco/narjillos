package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.PassNerve;
import org.nusco.swimmer.physics.Vector;


class NullOrgan extends Organ {

	public NullOrgan(Organ parent) {
		// TODO untested neuron
		super(0, 0, 0, 0, new PassNerve(), parent);
	}

	@Override
	public double getRelativeAngle() {
		return getParent().getRelativeAngle();
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
	public Organ getAsParent() {
		return getParent();
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

	@Override
	public String toString() {
		return "<null organ>";
	}
}
