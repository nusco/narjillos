package org.nusco.narjillos.creature.body.embryology;

import org.nusco.narjillos.creature.body.BodyPart;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.shared.utilities.ColorByte;

class OrganBuilder {
	
	static final double PART_LENGTH_MULTIPLIER = 1.0;
	static final double PART_THICKNESS_MULTIPLIER = 0.15;
	static final int PART_MAX_ANGLE_TO_PARENT = 70;

	public static final int MIN_LENGTH = 29;
	private static final int MIN_THICKNESS = 10;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Head buildHeadSystem() {
		Head result = new Head(getLength(), getThickness(), getHue(), getMetabolicRate());
		result.sproutNeck();
		return result;
	}

	public BodyPart buildBodyPart(BodyPart parent, int angleSign) {
		return parent.sproutOrgan(getLength(), getThickness(), getAngleToParentAtRest(angleSign), getHue(), getDelay());
	}

	private int getLengthGene() {
		if (genes[1] < MIN_LENGTH)
			return 0;  // atrophic organ
		return genes[1];
	}

	private int getThicknessGene() {
		if (genes[2] < MIN_THICKNESS)
			return MIN_THICKNESS;
		return genes[2];
	}

	private int getLength() {
		return (int)(getLengthGene() * PART_LENGTH_MULTIPLIER);
	}

	private int getThickness() {
		return (int)(getThicknessGene() * PART_THICKNESS_MULTIPLIER);
	}

	private int getDelay() {
		double maxDelay = 30;
		double maxByteValue = 255.0;
		double delayScale = maxByteValue / maxDelay;

		int delayGene = genes[3];
		return (int)(delayGene / delayScale);
	}

	private double getMetabolicRate() {
		double maxMetabolicRate = 4;
		double maxByteValue = 255.0;
		double rateScale = maxByteValue / maxMetabolicRate;

		int metabolicRateGene = genes[3];
		return metabolicRateGene / rateScale;
	}

	private int getAngleToParentAtRest(int angleSign) {
		int angleToParentGene = genes[4];
		int randomSign = (genes[4] & 1) == 1 ? -1 : 1;
		
		int result = (angleToParentGene % PART_MAX_ANGLE_TO_PARENT) * angleSign * randomSign;
		return result;
	}
	
	private ColorByte getHue() {
		return new ColorByte(genes[5]);
	}
}
