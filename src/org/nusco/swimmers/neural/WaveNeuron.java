package org.nusco.swimmers.neural;

import java.util.LinkedList;
import java.util.List;

public class WaveNeuron extends Neuron {

	private double angle = 90;
	private List<DelayNeuron> children = new LinkedList<>();
	
	@Override
	public double process(double frequency) {
		double result = Math.sin(Math.toRadians(angle));
		updateAngle(frequency);
		return result;
	}

	private void updateAngle(double frequency) {
		angle += 360 * frequency;
		angle = angle % 360;
	}

	public void connectTo(DelayNeuron box) {
		children.add(box);
	}
}
