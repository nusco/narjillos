package org.nusco.narjillos.embryogenesis;

import static org.nusco.narjillos.embryogenesis.CytogeneticLocation.*;

import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.genomics.Chromosome;

/**
 * Builds a Head from a chromosome.
 */
class HeadBuilder extends ConcreteOrganBuilder {

	public HeadBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	public double getMetabolicRate() {
		final double MAX_METABOLIC_RATE = 3;
		return getChromosome().getGene(METABOLIC_RATE) * (MAX_METABOLIC_RATE / 255);
	}

	public double getPercentEnergyToChildren() {
		return (getChromosome().getGene(PERCENT_ENERGY_TO_CHILDREN) + 1) / 256.0;
	}

	@Override
	public ConnectedOrgan buildOrgan(ConnectedOrgan parent, int ignored) {
		return new Head(getLength(), getThickness(), getHue(), getMetabolicRate(), getPercentEnergyToChildren());
	}
}
