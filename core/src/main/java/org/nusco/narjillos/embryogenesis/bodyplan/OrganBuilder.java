package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.ConnectedOrgan;

/**
 * Builds an organ according the the genes contained in a single chromosome.
 */
public interface OrganBuilder {

	public ConnectedOrgan buildOrgan(ConnectedOrgan parent, int sign);

	public BodyPlanInstruction getBodyPlanInstruction();
}
