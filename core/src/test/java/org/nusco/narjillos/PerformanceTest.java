package org.nusco.narjillos;

import org.nusco.narjillos.experiment.Experiment;

public class PerformanceTest {

	private final static int TICKS = 4000;

	public static void main(String[] args) {
		Experiment experiment = new Experiment("performance_test", 424242);

		long startTimeMillis = System.currentTimeMillis();
		
		for (int i = 0; i < TICKS; i++)
			experiment.tick();
		
		long endTimeMillis = System.currentTimeMillis();
		double timeMillis = (endTimeMillis - startTimeMillis) / 1000.0;
		double timeSeconds = Math.ceil(timeMillis * 10) / 10.0;
		
		System.out.println("" + TICKS + " cycles in " + timeSeconds + " seconds.");
		System.exit(0);
	}
}
