package org.nusco.swimmers.genetics;

import org.nusco.swimmers.body.Head;
import org.nusco.swimmers.body.Organ;
import org.nusco.swimmers.body.Swimmer;
import org.nusco.swimmers.body.VisibleOrgan;

public class DNA {
	public static final int GENES_PER_PART = 4;

	public static final int TERMINATE_PART = 0b11000000;

	private static final int PART_LENGTH_MULTIPLIER = 1;
	private static final double PART_THICKNESS_MULTIPLIER = 0.2;
	private static final int PART_MAX_RELATIVE_ANGLE = 90;

	private static final double MUTATION_RATE = 0.04;

	private int[] genes;

	public DNA(int... genes) {
		this.genes = normalize(genes);
	}

	private int[] normalize(int[] genes) {
		int[] result = new int[genes.length];
		for (int i = 0; i < genes.length; i++) {
			int normalized = genes[i] % 256;
			if(normalized < 0)
				result[i] = -normalized;
			else
				result[i] = normalized;
		}
		return result;
	}

	public int[] getGenes() {
		return genes;
	}

	public Swimmer toPhenotype() {
		DNAParser parser = new DNAParser(this);
		
		VisibleOrgan head = createHeadPart(parser.next());
		Organ child_1 = sproutNextPart(head, parser.next(), +1);
		Organ child_2 = sproutNextPart(head, parser.next(), -1);

		sproutNextPart(child_1, parser.next(), +1);
		sproutNextPart(child_1, parser.next(), -1);
		sproutNextPart(child_2, parser.next(), +1);
		sproutNextPart(child_2, parser.next(), -1);
		
		return new Swimmer(head);
	}

	private VisibleOrgan createHeadPart(int[] headGenes) {
		int length = headGenes[0] * PART_LENGTH_MULTIPLIER;
		int thickness = (int)(headGenes[1] * PART_THICKNESS_MULTIPLIER);
		int rgb = genes[3];
		return new Head(length, thickness, rgb);
	}

	private Organ sproutNextPart(Organ parent, int[] genes, int angleSign) {
		int length = genes[0] * PART_LENGTH_MULTIPLIER;
		int thickness = (int)(genes[1] * PART_THICKNESS_MULTIPLIER);
		int initialRelativeAngle = (genes[2] % PART_MAX_RELATIVE_ANGLE) * angleSign;
		int rgb = genes[3];
		if(length == 0 || thickness == 0)
			return parent.sproutInvisibleOrgan();
		return parent.sproutVisibleOrgan(length, thickness, initialRelativeAngle, rgb);
	}

	public DNA mutate() {
		int[] resultGenes = new int[genes.length];
		for (int i = 0; i < resultGenes.length; i++) {
			if(Math.random() < MUTATION_RATE)
				resultGenes[i] = (int)(Math.random() * 256);
			else
				resultGenes[i] = genes[i];
		}
		return new DNA(resultGenes);
	}
	
	public static DNA random() {
		final int genomeSize = 28;
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = rnd(20, 255);
		}
		return new DNA(genes);
	}

	public static DNA sample() {
		int[] genes =  new int[]{
									30, 20, 0, 0,
									25, 24, 30, 0,
									25, 24, 30, 0,
									15, 20, 30, 0,
									15, 20, 30, 0,
									15, 20, 30, 0,
									15, 20, 30, 0
								};
		return new DNA(genes);
	}

	private static int rnd(int min, int max) {
		return (int)(Math.random() * (max - min)) + min;
	}
}
