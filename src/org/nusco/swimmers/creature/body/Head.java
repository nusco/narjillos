package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.body.pns.TrackingNerve;
import org.nusco.swimmers.physics.Vector;

public class Head extends Organ {
	
	private Vector startPoint = Vector.ZERO;
	
	public Head(int length, int thickness, int rgb) {
		super(length, thickness, rgb, new TrackingNerve(), null);
		setAngle(0);
		tick(Vector.ZERO);
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector point) {
		this.startPoint = point;
	}

	@Override
	protected void move(Vector signal) {
		setAngle(signal.invert().getAngle());
	}

	public Organ sproutNeck() {
		return addChild(new Neck(this));
	}
}
