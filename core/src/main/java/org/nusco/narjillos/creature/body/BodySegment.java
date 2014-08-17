package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegment extends Organ {

	private final double angleToParentAtRest;
	private final int orientation; // -1 or 1
	private final int amplitude;

	public BodySegment(int length, int thickness, ColorByte hue, Organ parent, Nerve nerve, int angleToParentAtRest, int amplitude) {
		super(length, thickness, calculateColorMix(parent, hue), parent, nerve);
		this.angleToParentAtRest = angleToParentAtRest;
		this.orientation = (int) Math.signum(angleToParentAtRest);
		setAngleToParent(angleToParentAtRest);
		this.amplitude = amplitude;
	}

	public int getAmplitude() {
		return amplitude;
	}

	@Override
	protected double calculateAngleToParent(double targetAmplitudePercent, double skewing) {
		double correctedTargetAmplitudePercent = (orientation * targetAmplitudePercent * amplitude) + skewing;
		return angleToParentAtRest + correctedTargetAmplitudePercent + getForcedBend();
	}

	private static ColorByte calculateColorMix(Organ parent, ColorByte color) {
		if (parent == null)
			return color;
		return parent.getColor().mix(color);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, new ColorByte(0), null, new DelayNerve(13), 0, 1);
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	@Override
	public boolean equals(Object obj) {
		if (angleToParentAtRest != ((BodySegment) obj).angleToParentAtRest)
			return false;
		return super.equals(obj);
	}
}
