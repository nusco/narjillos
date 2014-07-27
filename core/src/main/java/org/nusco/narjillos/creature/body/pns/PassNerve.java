package org.nusco.narjillos.creature.body.pns;


/**
 * Always outputs the same vector it receives in input.
 */
public class PassNerve implements Nerve {

	@Override
	public double tick(double inputSignal) {
		return inputSignal;
	}
}
