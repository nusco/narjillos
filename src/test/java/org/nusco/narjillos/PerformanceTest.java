package org.nusco.narjillos;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.physics.FastMath;
import org.nusco.narjillos.experiment.Experiment;

/**
 * Runs a short experiment for a few thousands ticks and times the result.
 * It also prints an error message to get your attention in case the test
 * takes longer than a rule-of-thumb performance level (on my computer, 4000
 * ticks under 10 seconds).
 * 
 * It can be also run as a unit test. In this case, it goes through way fewer ticks,
 * so it's faster. This is by its very nature a nondeterministic test that can fail
 * randomly, or fail consistently on slower or overloaded machines - so it shouldn't
 * be run as part of an official build. But it's useful while developing, to catch
 * slowdowns as early as possible - so it's a good idea to put it in the local build
 * (assuming that your computer is exactly as fast as mine).
 */
public class PerformanceTest {

	private final static double EXPECTED_MINIMUM_TICKS_PER_SECOND = 400;
	
	private static int ticks;
	private static double timeSeconds;

	public static void main(String[] args) {
		ticks = 4000;
		try {
			new PerformanceTest().testPerformance();
		} catch (AssertionError e) {
			reportTicks();
			throw e;
		}
		reportTicks();
		System.exit(0); // exit Gradle
	}

	private static long getTicksPerSecond() {
		return Math.round(ticks / timeSeconds);
	}

	private static void reportTicks() {
		System.out.println("" + getTicksPerSecond() + " ticks per second");
	}

	@Before
	public void initializeTicks() {
		ticks = 100;
	}

	@Test
	public void testPerformance() {
		// pay up front for the setup of FastMath, in case
		// it hasn't been loaded yet
		FastMath.setUp();

		Experiment experiment = new SimpleExperiment();

		long startTimeMillis = System.currentTimeMillis();

		for (int i = 0; i < ticks; i++)
			experiment.tick();

		long endTimeMillis = System.currentTimeMillis();
		double timeMillis = (endTimeMillis - startTimeMillis) / 1000.0;
		timeSeconds = Math.ceil(timeMillis * 10) / 10.0;

		long tps = getTicksPerSecond();
		String errorMessage = "PERFORMANCE FAILURE: slower than expected (" + tps + " ticks per second)";
		assertTrue(errorMessage, tps > EXPECTED_MINIMUM_TICKS_PER_SECOND);
	}
}
