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
		// TODO: find a good way to deal with atrophy.
		// It should be possible for a Narjillo to have organs that are very short but wide.
		final int ATROPHY_LENGTH = 29;
		return chromosome.getGene(1) <= ATROPHY_LENGTH ? 0 : chromosome.getGene(1);
	}

	int getThickness() {
		final double MAX_THICKNESS = 50;
		return (int)(chromosome.getGene(2) * (MAX_THICKNESS / 256)) + 1;
	}

	int getDelay() {
		final double MAX_DELAY = 30;
		return (int)(chromosome.getGene(3) * ((MAX_DELAY + 1) / 256));
	}
	
	ColorByte getHue() {
		return new ColorByte(chromosome.getGene(4));
	}
}
