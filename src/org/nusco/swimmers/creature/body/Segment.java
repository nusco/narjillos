package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.DelayNerve;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

class Segment extends Organ {
	private static final int DELAY = 13;
	
	private double angle;

	public Segment(int length, int thickness, int relativeAngle, int rgb, Organ parent) {
		super(length, thickness, relativeAngle, rgb, new DelayNerve(DELAY), parent);
		this.angle = relativeAngle + parent.getAngle();
	}

	Segment(Nerve nerve) {
		this(0, 0, 0, 0, null);
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
		// special case: if the output is zero, try something random-ish
		if(outputSignal.getLength() < 0.01)
			outputSignal = Vector.polar(Math.random() * 30 + 30, Math.random() * 10 + 10);
		this.angle = getUpdatedAngle(outputSignal);
		return outputSignal;
	}
	
	private double getUpdatedAngle(Vector signal) {
		Vector signedSignal = signal.by(Math.signum(getRelativeAngle()));
		double angleToParent = getVector().plus(signedSignal).getAngle();
		System.out.println(angleToParent);
		return angleToParent; // + getParent().getAngle();
	}

	@Override
	public double getAngle() {
		return angle;
	}
}
