package org.nusco.narjillos;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.shared.physics.FastMath;
import org.nusco.narjillos.shared.utilities.Configuration;

/**
 * The program run by the "perftest" script. Runs a short experiment for a few
 * thousands ticks and times the result.
 */
public class PerformanceTest {

	private final static int TICKS = 4000;

	public static void main(String[] args) {
		FastMath.setUp(); // pay up front for the setup of FastMath

		Experiment experiment = new Experiment(424242, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, true), "performance_test", true);

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
