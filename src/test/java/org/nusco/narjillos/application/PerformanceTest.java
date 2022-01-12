package org.nusco.narjillos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.geometry.FastMath;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.experiment.SimpleExperiment;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;

/**
 * Runs a short experiment for a few thousands ticks and times the result.
 * It also prints an error message to get your attention in case the test
 * takes longer than a rule-of-thumb performance level (my computer,
 * an M1 Max MacBook Pro 2021, takes a bit less than 10 seconds to do
 * 10000 ticks).
 * <p>
 * It can be also run as a unit test. In .this case, it goes through way fewer ticks,
 * so it's faster. This is by its very nature a nondeterministic test that can fail
 * randomly, or fail consistently on slower or overloaded machines - so it shouldn't
 * be run as part of an official build. But it's useful while developing, to catch
 * slowdowns as early as possible - so it's a good idea to put it in the local build
 * (assuming that your computer is exactly as fast as mine).
 */
public class PerformanceTest {

	private final static double EXPECTED_MINIMUM_TICKS_PER_SECOND = 800;

	private static int ticks;

	private static double timeSeconds;

	private static final DNALog dnaLog = new PersistentDNALog("x");

	private static final HistoryLog historyLog = new PersistentHistoryLog("x");

	public static void main(String[] args) throws IOException {
		ticks = 20_000;
		try {
			new PerformanceTest().testPerformance();
		} catch (AssertionError e) {
			reportTicks();
			throw e;
		} finally {
			deleteDatabase();
		}
		reportTicks();
		System.exit(0); // exit Gradle
	}

	@AfterAll
	public static void deleteDatabase() {
		dnaLog.delete();
	}

	@BeforeEach
	public void initializeTicks() {
		ticks = 1500;
	}

	@Test
	public void testPerformance() {
		// pay up front for the setup of FastMath, in case
		// it hasn't been loaded yet
		FastMath.setUp();

		Experiment experiment = new SimpleExperiment();
		experiment.setDnaLog(dnaLog);
		experiment.setHistoryLog(historyLog);

		long startTimeMillis = System.currentTimeMillis();

		for (int i = 0; i < ticks; i++)
			experiment.tick();

		long endTimeMillis = System.currentTimeMillis();
		double timeMillis = (endTimeMillis - startTimeMillis) / 1000.0;
		timeSeconds = Math.ceil(timeMillis * 10) / 10.0;

		long tps = getTicksPerSecond();

		var errorMessage = "PERFORMANCE FAILURE: slower than expected (" + tps + " ticks per second)";
		assertThat(tps > EXPECTED_MINIMUM_TICKS_PER_SECOND)
				.as(errorMessage)
				.isTrue();
	}

	private static long getTicksPerSecond() {
		return Math.round(ticks / timeSeconds);
	}

	private static void reportTicks() {
		System.out.println("" + getTicksPerSecond() + " ticks per second");
	}
}
