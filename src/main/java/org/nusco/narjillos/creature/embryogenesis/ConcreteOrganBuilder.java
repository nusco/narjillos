package org.nusco.narjillos.creature.embryogenesis;

import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.BODY_PLAN_INSTRUCTION;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.LENGTH;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.THICKNESS;
import static org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction.BRANCH;
import static org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction.CONTINUE;
import static org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction.MIRROR;
import static org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction.SKIP;
import static org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction.STOP;

import org.nusco.narjillos.creature.embryogenesis.bodyplan.BodyPlanInstruction;
import org.nusco.narjillos.creature.embryogenesis.bodyplan.OrganBuilder;
import org.nusco.narjillos.genomics.Chromosome;

abstract class ConcreteOrganBuilder implements OrganBuilder {

	private final Chromosome chromosome;

	public ConcreteOrganBuilder(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	protected Chromosome getChromosome() {
		return chromosome;
	}

	int getLength() {
		final int ATROPHY_LENGTH = 29;
		return getChromosome().getGene(LENGTH) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(CytogeneticLocations.LENGTH);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int) (getChromosome().getGene(THICKNESS) * (MAX_THICKNESS / 256)) + 1;
	}

	@Override
	public BodyPlanInstruction getBodyPlanInstruction() {
		int bodyPlan = getChromosome().getGene(BODY_PLAN_INSTRUCTION) % 7;
		if (bodyPlan == 0)
			return CONTINUE;
		if (bodyPlan == 1)
			return SKIP;
		if (bodyPlan == 2 || bodyPlan == 3)
			return BRANCH;
		if (bodyPlan == 4)
			return MIRROR;
		return STOP;
	}
}
