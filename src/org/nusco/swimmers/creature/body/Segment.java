package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.DelayNerve;
import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.physics.Vector;

public class Segment extends Organ {
	private static final int DELAY = 21;
	private static final double AMPLITUDE_AMPLIFICATION = 4;
	
	protected final double angleToParentAtRest;

	public Segment(int length, int thickness, int angleToParentAtRest, int rgb, Organ parent) {
		super(length, thickness, rgb, new DelayNerve(DELAY), parent);
		this.angleToParentAtRest = angleToParentAtRest;
		setAngle(this.angleToParentAtRest + getParent().getAngle());
	}

	Segment(Nerve nerve) {
		this(0, 0, 0, 0, null);
	}

	@Override
	public int getColor() {
	    return getParent().getColor() & super.getColor();
	}
	
	public double getAngleToParentAtRest() {
		return angleToParentAtRest;
	}

	@Override
	protected void move(Vector signal) {
		double angle = getAngleToParentAtRest() + getParent().getAngle();
		Vector base = Vector.polar(angle, getLength());
		Vector impulse = signal.by(AMPLITUDE_AMPLIFICATION * Math.sin(angleToParentAtRest));
		Vector direction = base.plus(impulse);
		setAngle(direction.getAngle());
	}

	@Override
	public boolean equals(Object obj) {
		if(angleToParentAtRest != ((Segment)obj).getAngleToParentAtRest())
			return false;
		return super.equals(obj);
	}
}
