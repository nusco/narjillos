package org.nusco.narjillos.embryogenesis;

import org.nusco.narjillos.creature.body.BodySegment;
import org.nusco.narjillos.creature.body.Organ;
import org.nusco.narjillos.genomics.Chromosome;

class BodySegmentBuilder extends OrganBuilder {

	public BodySegmentBuilder(Chromosome chromosome) {
		super(chromosome);
	}

	int getDelay() {
		final double MAX_DELAY = 30;
		return (int)(getChromosome().getGene(Gene.METABOLIC_RATE_OR_DELAY) * ((MAX_DELAY + 1) / 256));
	}

	int getAngleToParent(int mirroringSign) {
		int result = convertToRange(getChromosome().getGene(Gene.ANGLE_TO_PARENT), (double) 70);
		return result * (int)Math.signum(mirroringSign);
	}

	int getAmplitude() {
		final double MAX_AMPLITUDE = 80;
		return (int)(getChromosome().getGene(Gene.PERCENT_ENERGY_TO_CHILDREN_OR_AMPLITUDE) * (MAX_AMPLITUDE / 256)) + 1;
	}

	public int getSkewing() {
		return convertToRange(getChromosome().getGene(Gene.SKEWING), (double) 90);
	}

	private int convertToRange(int gene, double maxAbsValue) {
		return (int)((gene * ((maxAbsValue * 2 + 1) / 256)) - maxAbsValue);
	}

	public Organ build(Organ parent, int angleSign) {
		return parent.addChild(new BodySegment(getLength(), getThickness(), getHue(), parent, getDelay(), getAngleToParent(angleSign), getAmplitude(), getSkewing()));
	}
}
