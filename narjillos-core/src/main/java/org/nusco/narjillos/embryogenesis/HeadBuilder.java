package org.nusco.narjillos.embryogenesis;

import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.*;

import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.genomics.Chromosome;
import org.nusco.narjillos.shared.utilities.Configuration;

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
	
	int getRed() {
		return getChromosome().getGene(RED);
	}
	
	int getGreen() {
		return getChromosome().getGene(GREEN);
	}
	
	int getBlue() {
		return getChromosome().getGene(BLUE);
	}

	double getPercentEnergyToChildren() {
		final double maxEnergyToChildrenFromGene = 1 - Configuration.CREATURE_MIN_PERCENT_ENERGY_TO_CHILDREN;
		double energyToChildrenFromGene = getChromosome().getGene(PERCENT_ENERGY_TO_CHILDREN) / 255.0 * maxEnergyToChildrenFromGene;
		return energyToChildrenFromGene + Configuration.CREATURE_MIN_PERCENT_ENERGY_TO_CHILDREN;
	}

	int getEggVelocity() {
		return getChromosome().getGene(EGG_VELOCITY);
	}

	int getEggInterval() {
		return getChromosome().getGene(EGG_INTERVAL) * 100;
	}

	@Override
	public MovingOrgan buildOrgan(ConnectedOrgan parent, int ignored) {
		return new Head(getLength(), getThickness(), getRed(), getGreen(), getBlue(), getMetabolicRate(), getPercentEnergyToChildren(), getEggVelocity(), getEggInterval());
	}
}
