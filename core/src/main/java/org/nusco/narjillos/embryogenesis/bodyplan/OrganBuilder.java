package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.Organ;

/**
 * Builds an organ according the the genes contained in a single chromosome.
 */
public interface OrganBuilder {

	public Organ buildOrgan(Organ parent, int sign);

	public BodyPlanInstruction getBodyPlanInstruction();
}
