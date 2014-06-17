package org.nusco.swimmers.creature.body;

import org.nusco.swimmers.creature.pns.Nerve;
import org.nusco.swimmers.creature.pns.WaveNerve;
import org.nusco.swimmers.physics.Vector;

public class Head extends Organ {
	
	private static final double FREQUENCY = 0.01;
	private Vector startPoint = Vector.ZERO;
	private double angle;

	public Head(int length, int thickness, int rgb) {
		super(length, thickness, 0, rgb, new WaveNerve(FREQUENCY), null);
		angle = getRelativeAngle();
	}

	Head(Nerve nerve) {
		super(0, 0, 0, 0, nerve, null);
		angle = getRelativeAngle();
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
		return -angle;
	}

	@Override
	public Vector tick(Vector inputSignal) {
		Vector result = super.tick(inputSignal);
//		correctAngle(inputSignal);
		return result;
	}

//	private void correctAngle(Vector inputSignal) {
//		if(angle - 180 < inputSignal.getAngle())
//			angle -= 0.5;
//		else if(angle - 180  > inputSignal.getAngle())
//			angle -= 0.5;
//	}
	
	public void placeAt(Vector point) {
		this.startPoint = point;
	}
}
