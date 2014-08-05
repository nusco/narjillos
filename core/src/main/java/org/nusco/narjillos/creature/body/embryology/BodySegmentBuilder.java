package org.nusco.narjillos.creature.body.embryology;

import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.creature.genetics.Chromosome;

class BodySegmentBuilder extends OrganBuilder {

	public BodySegmentBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	int getAngleToParent(int mirroringSign) {
		final double MAX_ABS_ANGLE_TO_PARENT = 70;
		int angleToParentGene = getChromosome().getGene(5);
		int result = (int)((angleToParentGene * ((MAX_ABS_ANGLE_TO_PARENT * 2 + 1) / 256)) - MAX_ABS_ANGLE_TO_PARENT);
		return result * (int)Math.signum(mirroringSign);
	}

	int getAmplitude() {
		final double MAX_AMPLITUDE = 80;
		return (int)(getChromosome().getGene(6) * (MAX_AMPLITUDE / 256)) + 1;
	}

	public BodySegment build(Organ parent, int angleSign) {
		return parent.sproutOrgan(getLength(), getThickness(), getHue(), getDelay(), getAngleToParent(angleSign), getAmplitude());
	}
}
