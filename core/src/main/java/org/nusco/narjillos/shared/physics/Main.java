package org.nusco.narjillos.shared.physics;

public class Main {
	public static void main(String[] args) {
		load();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10_000_000; i++) {
//			Math.sin(Math.toRadians(i));
//			Math.cos(Math.toRadians(i));
			FastMath.sin(i);
			FastMath.cos(i);
		}
		System.out.println((System.currentTimeMillis() - start) / 1000.0);
	}

	private static void load() {
		long start = System.currentTimeMillis();
		FastMath.sin(0);
		System.out.println((System.currentTimeMillis() - start) / 1000.0);
	}
}
