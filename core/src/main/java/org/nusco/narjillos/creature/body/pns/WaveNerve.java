package org.nusco.narjillos.creature.body.pns;


/**
 * Ignores the input signal and generates an output that goes from
 * -1 to 1 and back, in a sinusoidal wave.
 * 
 * The left semiplane of the sinusoidal wave (from +90 to -90 degrees) has a
 * higher frequency than the right semiplane (from -90 to 90). This generates
 * an organic-like motion, where organs move more slower in one direction and
 * quicker in the other.
 */
public class WaveNerve implements Nerve {

	private static final double BEAT_RATIO = 2;

	private final double frequency;

	private double angle = 0;

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}

	@Override
	public double tick(double ignored) {
		double result = Math.sin(angle);
		angle = update(angle);
		return result;
	}

	private double update(double currentAngle) {
		double multiplicationFactor = isInLeftSemiplane(currentAngle) ? BEAT_RATIO : 1;
		return (currentAngle + Math.PI * 2  * frequency * multiplicationFactor) % (Math.PI * 2);
	}

	private boolean isInLeftSemiplane(double currentAngle) {
		return currentAngle >= Math.PI / 2 && currentAngle < Math.PI / 2 * 3;
	}
}
