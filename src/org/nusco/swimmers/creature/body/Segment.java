package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.DelayNerve;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

class Segment extends Organ {
	private static final int DELAY = 13;
	
	private double angle;
	private final Side side;

	public Segment(int length, int thickness, int relativeAngle, Side side, int rgb, Organ parent) {
		super(length, thickness, relativeAngle, rgb, new DelayNerve(DELAY), parent);
		this.angle = relativeAngle + parent.getAngle();
		this.side = side;
	}

	Segment(Nerve nerve) {
		this(0, 0, 0, Side.LEFT, 0, null);
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
		this.angle = getUpdatedAngle(outputSignal);
		return outputSignal;
	}
	
	private double getUpdatedAngle(Vector signal) {
		return getVector().plus(signal.by(side.toSign())).getAngle();
	}

	@Override
	public double getAngle() {
		return angle;
	}
}
