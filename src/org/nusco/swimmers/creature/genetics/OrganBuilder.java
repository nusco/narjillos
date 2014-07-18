package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.BodyPart;

class OrganBuilder {
	
	public static final int MIN_ORGAN_SIZE = 25;

	static final double PART_LENGTH_MULTIPLIER = 1.0;
	static final double PART_THICKNESS_MULTIPLIER = 0.15;
	static final int PART_MAX_ANGLE_TO_PARENT = 70;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Head buildHeadSystem() {
		Head result = new Head(getLength(), getThickness(), getRGB());
		result.sproutNeck();
		return result;
	}

	public BodyPart buildSegment(BodyPart parent, int angleSign) {
		if(getLengthGenes() <= MIN_ORGAN_SIZE || getThicknessGenes() <= MIN_ORGAN_SIZE)
			return parent.sproutConnectiveTissue();
		return parent.sproutOrgan(getLength(), getThickness(), getAngleToParentAtRest(angleSign), getRGB());
	}

	private int getLengthGenes() {
		return genes[1];
	}

	private int getThicknessGenes() {
		return genes[2];
	}

	private int getLength() {
		return (int)(getLengthGenes() * PART_LENGTH_MULTIPLIER);
	}

	private int getThickness() {
		return (int)(getThicknessGenes() * PART_THICKNESS_MULTIPLIER);
	}

	private int getAngleToParentAtRest(int angleSign) {
		return (genes[3] % PART_MAX_ANGLE_TO_PARENT) * angleSign;
	}
	
	private int getRGB() {
		return genes[4];
	}
}
