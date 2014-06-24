package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.DelayNerve;
import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public class Segment extends Organ {
	private static final int DELAY = 21;
	private static final double AMPLITUDE_AMPLIFICATION = 4;
	
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
	public double getAngle() {
		return angle;
	}

	@Override
	protected void move(Vector signal) {
		Vector base = Vector.polar(getRelativeAngle(), getLength());
		Vector impulse = signal.by(AMPLITUDE_AMPLIFICATION * Math.sin(relativeAngle));
		Vector direction = base.plus(impulse);
		this.angle = direction.getAngle();
	}
}
