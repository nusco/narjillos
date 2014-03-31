package org.nusco.swimmer.genetics;

import org.nusco.swimmer.body.Organ;

class OrganBuilder {

	public static final int GENES_PER_PART = 4;

	static final int PART_LENGTH_MULTIPLIER = 1;
	static final double PART_THICKNESS_MULTIPLIER = 0.2;
	static final int PART_MAX_RELATIVE_ANGLE = 180;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Organ createHead() {
		return Organ.createHead(getLength(), getThickness(), getRGB());
	}

	public Organ createOrgan(Organ parent, int angleSign) {
		if(getLength() == 0 || getThickness() == 0)
			return parent.sproutInvisibleOrgan();
		return parent.sproutVisibleOrgan(getLength(), getThickness(), getInitialRelativeAngle(angleSign), getRGB());
	}

	int getLength() {
		return genes[0] * PART_LENGTH_MULTIPLIER;
	}

	int getThickness() {
		return (int)(genes[1] * PART_THICKNESS_MULTIPLIER);
	}

	int getInitialRelativeAngle(int angleSign) {
		return (genes[2] % PART_MAX_RELATIVE_ANGLE) * angleSign;
	}
	
	int getRGB() {
		return genes[3];
	}
}
