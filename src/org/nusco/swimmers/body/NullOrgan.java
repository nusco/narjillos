package org.nusco.swimmers.body;

import org.nusco.swimmers.neural.PassNeuron;


public class NullOrgan extends Organ {

	public NullOrgan(Organ parent) {
		// TODO untested neuron
		super(new PassNeuron(), parent);
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
	public String toString() {
		return "<null organ>";
	}
}
