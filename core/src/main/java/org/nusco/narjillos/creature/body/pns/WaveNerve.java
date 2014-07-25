package org.nusco.narjillos.creature.body.pns;

import org.nusco.narjillos.shared.physics.Vector;

/**
 * Given an input signal V, and its normal vector of the same length N, this
 * Nerve generates an output that goes from -N to N and back, in a sinusoidal
 * wave.
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
	public Vector tick(Vector inputSignal) {
		double amplitude = getCurrentAmplitude() * inputSignal.getLength();
		return inputSignal.getNormal().by(amplitude);
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
