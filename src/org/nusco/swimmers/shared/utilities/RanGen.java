package org.nusco.swimmers.shared.utilities;

import java.util.Random;

public class RanGen {

	private final static RanGen INSTANCE = new RanGen();
	
	private final Random random;
	
	private RanGen() {
		//long seed = (long)(Math.random() * Long.MAX_VALUE);
		long seed = 2648718169735535616l;
		System.out.println("Random seed: " + seed);
		random = new Random(seed);
	}

	public static double nextDouble() {
		return INSTANCE.random.nextDouble();
	}

	public static double nextGaussian() {
		return INSTANCE.random.nextGaussian();
	}

	public static int nextInt() {
		return INSTANCE.random.nextInt();
	}
}
