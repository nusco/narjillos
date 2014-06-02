package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.WaveNerve;
import org.nusco.swimmer.physics.Vector;

public class Head extends Organ {
	
	private static final double FREQUENCY = 0.01;
	private Vector startPoint = Vector.ZERO;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb, new WaveNerve(FREQUENCY), null);
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
		return 0;
	}

	public void placeAt(Vector point) {
		this.startPoint = point;
	}

	public final void tick() {
		getNerve().send(Vector.ZERO_ONE);
	}
}
