package org.nusco.swimmers.creature.genetics;

import org.nusco.swimmers.creature.body.Head;
import org.nusco.swimmers.creature.body.Organ;
import org.nusco.swimmers.creature.body.Side;

class OrganBuilder {
	public static final int GENES_PER_PART = 5;
	public static final int MIN_GENES_VALUE = 25;

	static final double PART_LENGTH_MULTIPLIER = 1.0;
	static final double PART_THICKNESS_MULTIPLIER = 0.15;
	static final int PART_MAX_RELATIVE_ANGLE = 100;

	private final int[] genes;

	public OrganBuilder(int[] partGenes) {
		this.genes = partGenes;
	}

	public Head buildHead() {
		return new Head(getLength(), getThickness(), getRGB());
	}

	public Organ buildSegment(Organ parent, Side side) {
		if(getLengthGenes() <= MIN_GENES_VALUE || getThicknessGenes() <= MIN_GENES_VALUE)
			return parent.sproutNullOrgan();
		return parent.sproutOrgan(getLength(), getThickness(), getRelativeAngle(side), side, getRGB());
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

	private int getRelativeAngle(Side side) {
		int angleSign = side == Side.RIGHT ? 1 : -1;
		int shift = PART_MAX_RELATIVE_ANGLE / 5;
		return (genes[3] % PART_MAX_RELATIVE_ANGLE - shift) * angleSign;
	}
	
	private int getRGB() {
		return genes[4];
	}
}
