package org.nusco.narjillos.embryogenesis;

import static org.nusco.narjillos.embryogenesis.bodyplan.BodyPlanInstruction.*;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocation.*;

import org.nusco.narjillos.embryogenesis.bodyplan.BodyPlanInstruction;
import org.nusco.narjillos.embryogenesis.bodyplan.OrganBuilder;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

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
		return getChromosome().getGene(LENGTH) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(CytogeneticLocation.LENGTH);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(getChromosome().getGene(THICKNESS) * (MAX_THICKNESS / 256)) + 1;
	}
	
	ColorByte getHue() {
		return new ColorByte(getChromosome().getGene(HUE));
	}

	@Override
	public BodyPlanInstruction getBodyPlanInstruction() {
		int bodyPlan = getChromosome().getGene(BODY_PLAN_INSTRUCTION) & 0b00000111;
		if (bodyPlan == 0 || bodyPlan == 1)
			return CONTINUE;
		if (bodyPlan == 2 || bodyPlan == 3)
			return BRANCH;
		if (bodyPlan == 4)
			return MIRROR;
		return STOP;
	}
}
