package org.nusco.narjillos.creature.embryogenesis;

import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.*;

import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.body.HeadParameters;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.genomics.Chromosome;

/**
 * Builds a Head from a chromosome.
 */
class HeadBuilder extends ConcreteOrganBuilder {

	public HeadBuilder(Chromosome chromosome) {
		super(chromosome);
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

	public double getMetabolicRate() {
		final double MAX_METABOLIC_RATE = 3;
		return getChromosome().getGene(METABOLIC_RATE) * (MAX_METABOLIC_RATE / 255);
	}

	public double getWaveBeatRatio() {
		final double minValue = 0.2;
		final double maxValue = 5.0;
		final double range = maxValue - minValue;
		final double ratio = 255 / range;
		return getChromosome().getGene(WAVE_BEAT_RATIO) / ratio + minValue;
	}

	public Element getByproduct() {
		return Element.fromInteger(getChromosome().getGene(BYPRODUCT));
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
		HeadParameters parameters = new HeadParameters();
		parameters.setAdultLength(getLength());
		parameters.setAdultThickness(getThickness());
		parameters.setRed(getRed());
		parameters.setGreen(getGreen());
		parameters.setBlue(getBlue());
		parameters.setMetabolicRate(getMetabolicRate());
		parameters.setWaveBeatRatio(getWaveBeatRatio());
		parameters.setByproduct(getByproduct());
		parameters.setEnergyToChildren(getEnergyToChildren());
		parameters.setEggVelocity(getEggVelocity());
		parameters.setEggInterval(getEggInterval());
		return new Head(parameters);
	}
}
