package org.nusco.narjillos.creature.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.MovingOrgan;

/**
 * Builds an organ according the genes contained in a single chromosome.
 */
public interface OrganBuilder {

	MovingOrgan buildOrgan(ConnectedOrgan parent, int sign);

	BodyPlanInstruction getBodyPlanInstruction();
}
