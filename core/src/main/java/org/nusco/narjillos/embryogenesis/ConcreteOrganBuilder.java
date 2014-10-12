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
		return getChromosome().getGene(Gene.LENGTH) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(Gene.LENGTH);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(getChromosome().getGene(Gene.THICKNESS) * (MAX_THICKNESS / 256)) + 1;
	}
	
	ColorByte getHue() {
		return new ColorByte(getChromosome().getGene(Gene.HUE));
	}

	@Override
	public Instruction getInstruction() {
		int bodyPlan = getChromosome().getGene(Gene.BODY_PLAN) & 0b00000111;
		if ((bodyPlan == 0b000) || (bodyPlan == 0b001))
			return Instruction.STOP;
		if ((bodyPlan == 0b010) || (bodyPlan == 0b011))
			return Instruction.CONTINUE;
		if ((bodyPlan == 0b100) || (bodyPlan == 0b101))
			return Instruction.BRANCH;
		return Instruction.MIRROR;
	}
}
