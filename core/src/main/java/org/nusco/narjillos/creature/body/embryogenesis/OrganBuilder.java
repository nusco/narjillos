package org.nusco.narjillos.creature.body.embryogenesis;

import org.nusco.narjillos.creature.genetics.Chromosome;
import org.nusco.narjillos.shared.utilities.ColorByte;

abstract class OrganBuilder {

	private final Chromosome chromosome;

	public OrganBuilder(Chromosome chromosome) {
		this.chromosome = chromosome;
	}

	protected Chromosome getChromosome() {
		return chromosome;
	}
	
	int getLength() {
		final int ATROPHY_LENGTH = 29;
		return getChromosome().getGene(1) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(1);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(getChromosome().getGene(2) * (MAX_THICKNESS / 256)) + 1;
	}
	
	ColorByte getHue() {
		return new ColorByte(getChromosome().getGene(4));
	}
}
