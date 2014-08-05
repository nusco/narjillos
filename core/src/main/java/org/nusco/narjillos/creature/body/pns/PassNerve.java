package org.nusco.narjillos.creature.body.pns;

/**
 * Outputs the same vector it receives as input.
 */
public class PassNerve implements Nerve {

	@Override
	public double tick(double inputSignal) {
		return inputSignal;
	}
}
