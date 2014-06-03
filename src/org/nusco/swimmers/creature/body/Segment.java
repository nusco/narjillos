package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.DelayNerve;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

class Segment extends Organ {
	private static final int DELAY = 13;
	
	private double angle;

	public Segment(int length, int thickness, int relativeAngle, int rgb, Organ parent) {
		super(length, thickness, relativeAngle, rgb, new DelayNerve(DELAY), parent);
		updateAngle(Vector.ZERO_ONE);
	}

	Segment(Nerve nerve) {
		super(0, 0, 0, 0, nerve, null);
		updateAngle(Vector.ZERO_ONE);
	}

	@Override
	public Organ getParent() {
		return parent;
	}

	@Override
	public Vector getStartPoint() {
		return parent.getEndPoint();
	}

	@Override
	public Vector tick(Vector inputSignal) {
		Vector outputSignal = super.tick(inputSignal);
		updateAngle(outputSignal);
		return outputSignal;
	}
	
	private void updateAngle(Vector outputSignal) {
		double relativeAngle = getRelativeAngle() * outputSignal.getLength();
		this.angle = relativeAngle + getParent().getAngle();
	}

	@Override
	public double getAngle() {
		return angle;
	}
}
