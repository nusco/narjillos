package org.nusco.narjillos.embryogenesis;

import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.Chromosome;

class HeadBuilder extends ConcreteOrganBuilder {

	public HeadBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	public double getMetabolicRate() {
		final double MAX_METABOLIC_RATE = 3;
		return getChromosome().getGene(CytogeneticLocation.METABOLIC_RATE) * (MAX_METABOLIC_RATE / 255);
	}

	public double getPercentEnergyToChildren() {
		return (getChromosome().getGene(CytogeneticLocation.PERCENT_ENERGY_TO_CHILDREN) + 1) / 256.0;
	}

	@Override
	public Organ buildOrgan(Organ parent, int ignored) {
		return new Head(getLength(), getThickness(), getHue(), getMetabolicRate(), getPercentEnergyToChildren());
	}
}
