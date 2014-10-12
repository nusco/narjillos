package org.nusco.narjillos.embryogenesis;

import org.nusco.narjillos.embryogenesis.bodyplan.Instruction;
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
		return getChromosome().getGene(CytogeneticLocation.LENGTH) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(CytogeneticLocation.LENGTH);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(getChromosome().getGene(CytogeneticLocation.THICKNESS) * (MAX_THICKNESS / 256)) + 1;
	}
	
	ColorByte getHue() {
		return new ColorByte(getChromosome().getGene(CytogeneticLocation.HUE));
	}

	@Override
	public Instruction getInstruction() {
		int bodyPlan = getChromosome().getGene(CytogeneticLocation.BODY_PLAN) & 0b00000111;
		if (bodyPlan == 0 || bodyPlan == 1)
			return Instruction.CONTINUE;
		if (bodyPlan == 2 || bodyPlan == 3)
			return Instruction.BRANCH;
		if (bodyPlan == 4)
			return Instruction.MIRROR;
		return Instruction.STOP;
	}
}
