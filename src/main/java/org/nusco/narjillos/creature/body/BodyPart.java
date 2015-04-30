package org.nusco.narjillos.creature.body;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.creature.body.pns.DelayNerve;
import org.nusco.narjillos.creature.body.pns.Nerve;

/**
 * A segment in the body of a creature.
 */
public class BodyPart extends MovingOrgan {

	private final double angleToParentAtRest;
	private final int orientation; // -1 or 1
	private final int amplitude;
	private final int skewing;

	private double currentSkewing = 0;
	private double cachedMetabolicRate = -1;

	public BodyPart(int adultLength, int adultThickness, int redShift, int greenShift, int blueShift, ConnectedOrgan parent, int delay, int angleToParentAtRest, int amplitude, int skewing) {
		super(adultLength,
			  adultThickness,
			  parent.getFiber().shift(redShift, greenShift, blueShift),
			  parent,
			  new DelayNerve(delay),
			  angleToParentAtRest);
		this.angleToParentAtRest = angleToParentAtRest;
		this.orientation = (int) Math.signum(angleToParentAtRest);
		this.amplitude = amplitude;
		this.skewing = skewing;
	}

	BodyPart(Nerve nerve) {
		this(0, 0, 0, 0, 0, null, 13, 0, 1, 0);
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

	public int getSkewing() {
		return skewing;
	}

	public int getDelay() {
		return ((DelayNerve) getNerve()).getDelay();
	}

	@Override
	protected double calculateNewAngleToParent(double targetAmplitudePercent, double angleToTarget) {
		double unbentAmplitude = orientation * targetAmplitudePercent * amplitude;
		return angleToParentAtRest + unbentAmplitude + calculateSkewing(angleToTarget);
	}
	
	@Override
	protected double calculateAbsoluteAngle() {
		return getParent().getAbsoluteAngle() + getAngleToParent();
	}

	@Override
	protected double getMetabolicRate() {
		if (cachedMetabolicRate == -1)
			cachedMetabolicRate = getParent().getMetabolicRate();
		return cachedMetabolicRate;
	}

	protected double calculateSkewing(double angleToTarget) {
		double targetSkewing = (angleToTarget % 180) / 180 * getSkewing();
		currentSkewing += getSkewingVelocity(targetSkewing);
		return currentSkewing;
	}

	private double getSkewingVelocity(double targetSkewing) {
		double result = targetSkewing - currentSkewing;
		double maxSkewingVelocity = getMetabolicRate() * Configuration.CREATURE_BASE_SKEWING_VELOCITY;
		if (Math.abs(result) > maxSkewingVelocity)
			return Math.signum(result) * maxSkewingVelocity;
		return result;
	}
}
