package org.nusco.swimmer.genetics;

import org.nusco.swimmer.body.Organ;
import org.nusco.swimmer.physics.Vector;

class OrganBuilder {
	public static final int GENES_PER_PART = 5;
	public static final int MIN_GENES_VALUE = 25;

	static final double PART_LENGTH_MULTIPLIER = 1.0;
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
		if(getLengthGenes() <= MIN_GENES_VALUE || getThicknessGenes() <= MIN_GENES_VALUE)
			return parent.sproutInvisibleOrgan();
		return parent.sproutVisibleOrgan(new Vector(getLength(), 0), getLength(), getThickness(), getRelativeAngle(angleSign), getRGB());
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

	private int getRelativeAngle(int angleSign) {
		return (genes[3] % PART_MAX_RELATIVE_ANGLE) * angleSign;
	}
	
	private int getRGB() {
		return genes[4];
	}
}
