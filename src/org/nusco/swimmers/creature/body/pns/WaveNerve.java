package org.nusco.swimmers.creature.body.pns;

import org.nusco.swimmers.physics.Vector;

/**
 * Given an input vector V, and its normal vector of the same length N, this
 * Nerve generates an output that goes from -N to N and back, in a sinusoidal
 * wave.
 * 
 * The negative part of the sinusoidal wave has a higher frequency than the
 * positive part. This generates a life-like motion, where organs move slowly
 * in one direction, and then return quickly to the original position.
 */
public class WaveNerve implements Nerve {

	private static final double AMPLITUDE = 10;

	private final double frequency;

	private double currentAngle = 0;

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}

	@Override
	public Vector send(Vector inputSignal) {
		double inputSignalLength = inputSignal.getLength() * AMPLITUDE;
		Vector inputSignalNormal = inputSignal.getNormal();
		double amplitude = getCurrentAmplitude() * inputSignalLength;
		return inputSignalNormal.by(amplitude);
	}

	private double getCurrentAmplitude() {
		double amplitude = Math.cos(currentAngle);
		currentAngle = update(currentAngle);
		return amplitude;
	}

	private double update(double currentAngle) {
		double multFactor = (currentAngle > Math.PI) ? 1 : 1.5;
		return (currentAngle + Math.PI * 2 * multFactor  * frequency) % (Math.PI * 2);
	}
}
