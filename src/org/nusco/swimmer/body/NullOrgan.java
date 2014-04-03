package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.NerveBuilder;
import org.nusco.swimmer.physics.Vector;


class NullOrgan extends Organ {

	public NullOrgan(Organ parent) {
		// TODO untested neuron
		super(NerveBuilder.createPassNerve(), parent);
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

	public boolean isVisible() {
		return false;
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
	public void tick() {
	}

	@Override
	public String toString() {
		return "<null organ>";
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NullOrgan))
			return false;
		NullOrgan other = (NullOrgan) obj;
		if (!getParent().equals(other.getParent()))
			return false;
		return true;
	}
}
