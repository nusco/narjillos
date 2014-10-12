package org.nusco.narjillos.embryogenesis.bodyplan;

import org.nusco.narjillos.creature.body.Organ;

public interface OrganBuilder {

	public Organ buildOrgan(Organ parent, int sign);
	public BodyPlanInstruction getInstruction();
}
