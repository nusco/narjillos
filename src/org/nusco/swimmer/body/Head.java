package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.NerveBuilder;
import org.nusco.swimmer.physics.Vector;


class Head extends VisibleOrgan {
	
	private static final double FREQUENCY = 0.01;
	private Vector startPoint = Vector.ZERO;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb, NerveBuilder.createWaveNerve(), null);
	}

	@Override
	public Vector getStartPoint() {
		return startPoint;
	}

	@Override
	public double getAngle() {
		return 0;
	}

	@Override
	public Organ getParent() {
		return null;
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}

	public void tick() {
		getNerve().send(FREQUENCY);
	}
}
