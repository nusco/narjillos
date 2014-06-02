package org.nusco.swimmers.creature.pns;

import java.util.LinkedList;
import java.util.List;

import org.nusco.swimmers.physics.Vector;

public class WaveNerve extends Nerve {

	private final double frequency;

	private double angle = 0;
	private List<DelayNerve> children = new LinkedList<>();

	public WaveNerve(double frequency) {
		this.frequency = frequency;
	}
	
	@Override
	public Vector process(Vector inputSignal) {
		double amplitude = Math.cos(Math.toRadians(angle));
		updateAngle();
		return inputSignal.by(amplitude);
	}

	private void updateAngle() {
		angle += 360 * frequency;
		angle = angle % 360;
	}

	public void connectTo(DelayNerve box) {
		children.add(box);
	}
}
