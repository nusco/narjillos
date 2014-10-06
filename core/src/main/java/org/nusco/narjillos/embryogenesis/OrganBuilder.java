package org.nusco.narjillos.embryogenesis;

import org.nusco.narjillos.genomics.Chromosome;
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
		return getChromosome().getGene(Gene.LENGTH) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(1);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(getChromosome().getGene(Gene.THICKNESS) * (MAX_THICKNESS / 256)) + 1;
	}
	
	ColorByte getHue() {
		return new ColorByte(getChromosome().getGene(Gene.PERCENT_ENERGY_TO_CHILDREN_OR_HUE));
	}
}
