package org.nusco.narjillos.creature.body.pns;


/**
 * Generates an output that goes from -1 to 1 and back, in a sinusoidal
 * wave. The input signal is a skew value that will be added to the current
 * output signal. For example, if you input -2 continuously, you will get a
 * wave from -3 to -1.
 * 
 * The negative part of the sinusoidal wave has a higher frequency than the
 * part part. This generates a life-like motion, where organs move more slowly
 * in one direction, and more quickly in the other.
 */
public class WaveNerve implements Nerve {

	private static final double BEAT_RATIO = 2;

	private final double frequency;

	private double currentAngle = Math.PI / 2;

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}

	@Override
	public double tick(double skew) {
		return skew + getCurrentAmplitude();
	}

	private double getCurrentAmplitude() {
		double amplitude = Math.cos(currentAngle);
		currentAngle = update(currentAngle);
		return amplitude;
	}

	private double update(double currentAngle) {
		boolean positiveSide = currentAngle > 0 && currentAngle <= Math.PI;
		double multFactor = positiveSide ? 1 : BEAT_RATIO;
		return (currentAngle + Math.PI * 2  * frequency * multFactor) % (Math.PI * 2);
	}
}
