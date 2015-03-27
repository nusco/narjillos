package org.nusco.narjillos.creature.body.pns;

import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.utilities.Configuration;

/**
 * Generates an output that goes from -1 to 1 and back, in a sinusoidal wave.
 * Ignores the input signal.
 * 
 * The left semiplane of the sinusoidal wave (from +90 to -90 degrees) has a
 * higher frequency than the right semiplane (from -90 to 90). This generates
 * an organic-like motion, where organs move more slower in one direction and
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
	public double tick(double ignored) {
		double result = FastMath.sin(angle);
		angle = update(angle);
		return result;
	}

	private double update(double currentAngle) {
		double multiplicationFactor = isInLeftSemiplane(currentAngle) ? Configuration.CREATURE_WAVE_BEAT_RATIO : 1;
		return (currentAngle + 360 * frequency * multiplicationFactor) % 360;
	}

	private boolean isInLeftSemiplane(double currentAngle) {
		return currentAngle >= 90 && currentAngle < 270;
	}
}
