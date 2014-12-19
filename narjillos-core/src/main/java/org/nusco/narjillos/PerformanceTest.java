package org.nusco.narjillos;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.shared.physics.FastMath;

/**
 * The program run by the _perftest_ script. Runs a short experiment for a few
 * thousands ticks and times the result.
 */
public class PerformanceTest {

	private final static int TICKS = 4000;

	public static void main(String[] args) {
		FastMath.setUp(); // pay up front for the setup of FastMath

		Experiment experiment = new Experiment(424242, "performance_test");

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
