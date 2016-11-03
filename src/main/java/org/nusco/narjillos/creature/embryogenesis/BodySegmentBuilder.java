package org.nusco.narjillos.creature.embryogenesis;

import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.AMPLITUDE;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.ANGLE_TO_PARENT;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.BLUE_SHIFT;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.DELAY;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.GREEN_SHIFT;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.RED_SHIFT;
import static org.nusco.narjillos.creature.embryogenesis.CytogeneticLocations.SKEWING;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.BodyPartParameters;
import org.nusco.narjillos.creature.body.ConnectedOrgan;
import org.nusco.narjillos.creature.body.MovingOrgan;
import org.nusco.narjillos.genomics.Chromosome;

/**
 * Builds a BodySegment from a chromosome.
 */
class BodySegmentBuilder extends ConcreteOrganBuilder {

	public BodySegmentBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	int getDelay() {
		final double MAX_DELAY = 30;
		return (int) (getChromosome().getGene(DELAY) * ((MAX_DELAY + 1) / 256));
	}

	int getAngleToParent(int mirroringSign) {
		int result = convertToRange(getChromosome().getGene(ANGLE_TO_PARENT), (double) 70);
		return result * (int) Math.signum(mirroringSign);
	}

	int getAmplitude() {
		final double MAX_AMPLITUDE = 80;
		return (int) (getChromosome().getGene(AMPLITUDE) * (MAX_AMPLITUDE / 256)) + 1;
	}

	public int getSkewing() {
		return convertToRange(getChromosome().getGene(SKEWING), (double) 90);
	}

	private int convertToRange(int gene, double maxAbsValue) {
		return (int) ((gene * ((maxAbsValue * 2 + 1) / 256)) - maxAbsValue);
	}

	int getRedShift() {
		return getFiberShift(RED_SHIFT);
	}

	int getGreenShift() {
		return getFiberShift(GREEN_SHIFT);
	}

	int getBlueShift() {
		return getFiberShift(BLUE_SHIFT);
	}

	private int getFiberShift(int redShift) {
		return (int) ((getChromosome().getGene(redShift) - 127.5) * 2);
	}

	@Override
	public MovingOrgan buildOrgan(ConnectedOrgan parent, int sign) {
		BodyPartParameters bodyPartParameters = new BodyPartParameters(getLength(), getThickness(), parent, getAngleToParent(sign));
		bodyPartParameters.setDelay(getDelay());
		bodyPartParameters.setAmplitude(getAmplitude());
		bodyPartParameters.setSkewing(getSkewing());
		bodyPartParameters.setRedShift(getRedShift());
		bodyPartParameters.setGreenShift(getGreenShift());
		bodyPartParameters.setBlueShift(getBlueShift());
		return new BodyPart(bodyPartParameters);
	}
}
