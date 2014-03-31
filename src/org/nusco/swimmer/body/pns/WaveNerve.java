package org.nusco.swimmer.body.pns;

import java.util.LinkedList;
import java.util.List;

class WaveNerve extends Nerve {

	private double angle = 90;
	private List<DelayNerve> children = new LinkedList<>();
	
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

	public void connectTo(DelayNerve box) {
		children.add(box);
	}
}
