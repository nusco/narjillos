package org.nusco.narjillos.creature.body.pns;

/**
 * Processes nervous impulses. You give it an input, it gives back an output.
 */
public interface Nerve {

	double tick(double inputSignal);
}
