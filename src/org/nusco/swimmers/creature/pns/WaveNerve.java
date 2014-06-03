package org.nusco.swimmers.creature.pns;

import org.nusco.swimmers.physics.Vector;

/**
 * Given an input vector V, and its normal vector of the same length N,
 * this Nerve generates an output that goes from -N to N and back, in a sinusoidal wave.
 */
public class WaveNerve extends Nerve {

	// TODO: remove this once we have a better policy for input signals
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
		return (currentAngle + Math.PI * 2 * frequency) % (Math.PI * 2);
	}
}
