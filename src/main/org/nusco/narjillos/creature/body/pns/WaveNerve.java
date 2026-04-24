package org.nusco.narjillos.creature.body.pns;

import org.nusco.narjillos.core.geometry.FastMath;

/**
 * Generates an output that goes from -1 to 1 and back, in a sinusoidal wave.
 * Ignores the input signal.
 * <p>
 * The left semiplane of the sinusoidal wave (from +90 to -90 degrees) has a
 * higher frequency than the right semiplane (from -90 to 90). This generates
 * an organic-like motion, where organs move slower in one direction and
 * quicker in the other.
 */
public class WaveNerve implements Nerve {

	private final double frequency;

	private double angle = 0;

	static {
		FastMath.setUp();
	}

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}

	@Override
	public double tick(double beatRatio) {
		double result = FastMath.sin(angle);
		angle = update(angle, beatRatio);
		return result;
	}

	public double getAngle() {
		return angle;
	}

	private double update(double currentAngle, double beatRatio) {
		double multiplicationFactor = isInLeftSemiplane(currentAngle) ? beatRatio : 1;
		return (currentAngle + 360 * frequency * multiplicationFactor) % 360;
	}

	private boolean isInLeftSemiplane(double currentAngle) {
		return currentAngle >= 90 && currentAngle < 270;
	}
}
