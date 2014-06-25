package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.Nerve;
import org.nusco.swimmers.creature.body.pns.TrackingNerve;
import org.nusco.swimmers.physics.Vector;

public class Head extends Organ {
	
	private Vector startPoint = Vector.ZERO;
	private double angle;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb, new TrackingNerve(), null);
		angle = getAngleToParentAtRest();
	}

	Head(Nerve nerve) {
		super(0, 0, 0, 0, nerve, null);
		angle = getAngleToParentAtRest();
	}

	@Override
	public Organ getParent() {
		return null;
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	@Override
	public double getAngle() {
		return angle;
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}

	@Override
	protected void move(Vector signal) {
	}

	public Organ sproutNeck() {
		return addChild(new Neck(this));
	}
}
