package org.nusco.swimmers.body;

import org.nusco.swimmers.neural.DelayNeuron;


public class BodyPart extends VisibleOrgan {
	private static final int DELAY = 13;
	
	public BodyPart(int length, int thickness, int relativeAngle, int rgb, Organ parent) {
		super(length, thickness, relativeAngle, rgb, new DelayNeuron(DELAY), parent);
	}

	public Organ getParent() {
		return parent.getAsParent();
	}
	
	public Vector getStartPoint() {
		return parent.getEndPoint();
	}

	@Override
	public double getAngle() {
		double relativeAngle = getRelativeAngle();
		double absoluteAngle = relativeAngle + getParent().getAngle();
		double amplifiedAngle = absoluteAngle * getNeuron().readOutputSignal();
		return Angle.normalize(amplifiedAngle);
	}
}
