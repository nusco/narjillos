package org.nusco.narjillos.embryogenesis;

import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.BLUE;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.EGG_INTERVAL;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.EGG_VELOCITY;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.ENERGY_TO_CHILDREN;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.GREEN;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.METABOLIC_RATE;
import static org.nusco.narjillos.embryogenesis.CytogeneticLocations.RED;

import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
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

	double getEnergyToChildren() {
		return getChromosome().getGene(ENERGY_TO_CHILDREN) * 100 + Configuration.CREATURE_MIN_ENERGY_TO_CHILDREN;
	}

	int getEggVelocity() {
		return getChromosome().getGene(EGG_VELOCITY);
	}

	int getEggInterval() {
		return getChromosome().getGene(EGG_INTERVAL) * 100;
	}

	@Override
	public MovingOrgan buildOrgan(ConnectedOrgan parent, int ignored) {
		return new Head(getLength(), getThickness(), getRed(), getGreen(), getBlue(), getMetabolicRate(), getEnergyToChildren(), getEggVelocity(), getEggInterval());
	}
}
