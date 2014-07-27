package org.nusco.narjillos.creature.body.pns;


/**
 * Processes nervous impulses, turning input signals into
 * (generally different) output signals.
 */
public interface Nerve {

	public double tick(double inputSignal);
}
