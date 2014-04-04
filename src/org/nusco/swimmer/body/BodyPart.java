package org.nusco.swimmer.body;

import org.nusco.swimmer.body.pns.Nerve;
import org.nusco.swimmer.body.pns.NerveBuilder;
import org.nusco.swimmer.physics.Angle;
import org.nusco.swimmer.physics.Vector;


class BodyPart extends VisibleOrgan {
	private static final int DELAY = 13;
	
	public BodyPart(Vector relativeVector, int length, int thickness, int relativeAngle, int rgb, Organ parent) {
		super(relativeVector, length, thickness, relativeAngle, rgb, createNerve(), parent);
	}

	private static Nerve createNerve() {
		return NerveBuilder.createDelayNerve(DELAY);
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
		double amplifiedAngle = absoluteAngle * getNerve().readOutputSignal().getLength();
		return Angle.normalize(amplifiedAngle);
	}
}
