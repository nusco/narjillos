package org.nusco.swimmers.shared.utilities;

import java.util.Random;

public class RanGen {

	private final static RanGen INSTANCE = new RanGen();
	
	private final Random random;
	
	private RanGen() {
		long seed = (long)(Math.random() * Long.MAX_VALUE);
		System.out.println("Random seed: " + seed);
		random = new Random(seed);
	}

	private double nextDouble() {
		return random.nextDouble();
	}
	
	public static double next() {
		return INSTANCE.nextDouble();
	}
}
