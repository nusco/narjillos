package org.nusco.narjillos.embryogenesis;

import static org.nusco.narjillos.embryogenesis.CytogeneticLocation.*;

import org.nusco.narjillos.creature.body.BodyPart;
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
		return (int)(getChromosome().getGene(DELAY) * ((MAX_DELAY + 1) / 256));
	}

	int getAngleToParent(int mirroringSign) {
		int result = convertToRange(getChromosome().getGene(ANGLE_TO_PARENT), (double) 70);
		return result * (int)Math.signum(mirroringSign);
	}

	int getAmplitude() {
		final double MAX_AMPLITUDE = 80;
		return (int)(getChromosome().getGene(AMPLITUDE) * (MAX_AMPLITUDE / 256)) + 1;
	}

	public int getSkewing() {
		return convertToRange(getChromosome().getGene(SKEWING), (double) 90);
	}

	private int convertToRange(int gene, double maxAbsValue) {
		return (int)((gene * ((maxAbsValue * 2 + 1) / 256)) - maxAbsValue);
	}

	@Override
	public MovingOrgan buildOrgan(ConnectedOrgan parent, int sign) {
		return new BodyPart(getLength(), getThickness(), getHue(), parent, getDelay(), getAngleToParent(sign), getAmplitude(), getSkewing());
	}
}
