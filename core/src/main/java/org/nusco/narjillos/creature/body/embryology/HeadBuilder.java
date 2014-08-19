package org.nusco.narjillos.creature.body.embryology;

import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.genetics.Chromosome;

class HeadBuilder extends OrganBuilder {

	public HeadBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	public double getMetabolicRate() {
		final double MAX_METABOLIC_RATE = 3;
		return getChromosome().getGene(3) * (MAX_METABOLIC_RATE / 255);
	}

	public Head build() {
		return new Head(getLength(), getThickness(), getHue(), getMetabolicRate());
	}
}
