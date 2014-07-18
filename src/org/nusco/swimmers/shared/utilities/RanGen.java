package org.nusco.swimmers.shared.utilities;

import java.util.Random;

public class RanGen {

	private final static RanGen INSTANCE = new RanGen();
	
	private final Random random;
	
	private RanGen() {
		//long seed = (long)(Math.random() * Long.MAX_VALUE);
		long seed = 5306841226579940352l;
		System.out.println("Random seed: " + seed);
		random = new Random(seed);
	}

	public static double nextDouble() {
		return INSTANCE.random.nextDouble();
	}

	public static double nextGaussian() {
		return INSTANCE.random.nextGaussian();
	}
}
