package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

public class BodySegment extends Organ {

	private final double angleToParentAtRest;
	private final int orientation; // -1 or 1
	private final int amplitude;

	public BodySegment(int length, int thickness, ColorByte hue, Organ parent, int delay, int angleToParentAtRest, int amplitude) {
		super(length, thickness, calculateColorMix(parent, hue), parent, new DelayNerve(delay));
		this.angleToParentAtRest = angleToParentAtRest;
		this.orientation = (int) Math.signum(angleToParentAtRest);
		setAngleToParent(angleToParentAtRest);
		this.amplitude = amplitude;
	}

	public double getAngleToParentAtRest() {
		return angleToParentAtRest;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getAmplitude() {
		return amplitude;
	}

	public int getDelay() {
		return ((DelayNerve) getNerve()).getDelay();
	}

	@Override
	protected double calculateNewAngleToParent(double targetAmplitudePercent) {
		double unbentAmplitude = orientation * targetAmplitudePercent * amplitude;
		return angleToParentAtRest + unbentAmplitude + getSkewing();
	}

	private static ColorByte calculateColorMix(Organ parent, ColorByte color) {
		if (parent == null)
			return color;
		return parent.getColor().mix(color);
	}

	BodySegment(Nerve nerve) {
		this(0, 0, new ColorByte(0), null, 13, 0, 1);
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}
}
