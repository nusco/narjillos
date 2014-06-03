package org.nusco.swimmers.creature.pns;

import org.nusco.swimmers.physics.Vector;

/**
 * Given an input V, generates an output that goes from -V to V and back in a sinusoidal wave.
 */
public class WaveNerve extends Nerve {

	private final double frequency;

	private double cosWave = 0;

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}
	
	@Override
	public Vector send(Vector inputSignal) {
		double amplitude = Math.cos(cosWave);
		updateCosWave();
		return inputSignal.by(amplitude);
	}

	private void updateCosWave() {
		cosWave = (cosWave + Math.PI * 2 * frequency) % (Math.PI * 2);
	}
}
