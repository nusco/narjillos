package org.nusco.swimmer.genetics;

import org.nusco.swimmer.body.Organ;

class OrganBuilder {

	public static final int GENES_PER_PART = 5;

	static final double PART_LENGTH_MULTIPLIER = 1;
	static final double PART_THICKNESS_MULTIPLIER = 0.15;
	static final int PART_MAX_RELATIVE_ANGLE = 120;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Organ buildHead() {
		return Organ.createHead(getLength(), getThickness(), getRGB());
	}

	public Organ buildBodyPart(Organ parent, int angleSign) {
		if(getLength() <= 10 || getThickness() <= 4)
			return parent.sproutInvisibleOrgan();
		return parent.sproutVisibleOrgan(getLength(), getThickness(), getRelativeAngle(angleSign), getRGB());
	}

	private int getLength() {
		return (int)(genes[1] * PART_LENGTH_MULTIPLIER);
	}

	private int getThickness() {
		return (int)(genes[2] * PART_THICKNESS_MULTIPLIER);
	}

	private int getRelativeAngle(int angleSign) {
		return (genes[3] % PART_MAX_RELATIVE_ANGLE) * angleSign;
	}
	
	private int getRGB() {
		return genes[4];
	}
}
