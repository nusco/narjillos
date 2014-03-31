package org.nusco.swimmers.genetics;



public class DNA {
	public static final int TERMINATE_PART = 0b11000000;
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

	public DNA mutate() {
		int[] resultGenes = new int[genes.length];
		for (int i = 0; i < resultGenes.length; i++) {
			if(Math.random() < MUTATION_RATE)
				resultGenes[i] = mutate(resultGenes, i);
			else
				resultGenes[i] = genes[i];
		}
		return new DNA(resultGenes);
	}

	private int mutate(int[] resultGenes, int i) {
		int shift = (int)(Math.random() * 8);
		int xorMask = 0b000000001 << shift;
		return resultGenes[i] ^ xorMask;
	}

	public static DNA random() {
		final int genomeSize = 60;
		int[] genes = new int[genomeSize];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = rnd(0, 255);
		}
		return new DNA(genes);
	}

	public static DNA sample() {
		int[] genes =  new int[]{
									60, 40, 0, 0,
									50, 48, 30, 0,
									50, 48, 30, 0,
									30, 40, 30, 0,
									30, 40, 30, 0,
									30, 40, 30, 0,
									30, 40, 30, 0,
									50, 30, 20, 123,
									50, 30, -20, 123,
									50, 30, 20, 123,
									50, 30, -20, 123
								};
		return new DNA(genes);
	}

	private static int rnd(int min, int max) {
		return (int)(Math.random() * (max - min)) + min;
	}
}
