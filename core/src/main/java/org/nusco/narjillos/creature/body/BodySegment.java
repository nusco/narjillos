package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.creature.body.physics.ForceField;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;
import org.nusco.narjillos.shared.utilities.ColorByte;

class BodySegment extends Organ {

	private static final int FIXED_MAX_AMPLITUDE_THAT_SHOULD_ACTUALLY_BE_GENETICALLY_DETERMINED = 45;

	private final double angleToParentAtRest;
	private final int orientation; // -1 or 1

	public BodySegment(int length, int thickness, ColorByte hue, Nerve nerve, int angleToParentAtRest, Organ parent) {
		super(length, thickness, calculateColorMix(parent, hue), parent, nerve);
		this.angleToParentAtRest = angleToParentAtRest;
		this.orientation = (int) Math.signum(angleToParentAtRest);
		setAngleToParent(angleToParentAtRest);
	}

	// TODO: remove
	BodySegment(Nerve nerve) {
		this(0, 0, new ColorByte(0), new DelayNerve(13), 0, null);
	}

	@Override
	protected double calculateAngleToParent(double targetAmplitudePercent, double skewing, ForceField forceField) {
		double correctedTargetAmplitudePercent = (orientation * targetAmplitudePercent * FIXED_MAX_AMPLITUDE_THAT_SHOULD_ACTUALLY_BE_GENETICALLY_DETERMINED) + skewing;
		return angleToParentAtRest + correctedTargetAmplitudePercent + getForcedBend();
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

	private static ColorByte calculateColorMix(Organ parent, ColorByte color) {
		return parent.getColor().mix(color);
	}
}
