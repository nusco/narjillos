package org.nusco.narjillos;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.pond.Cosmos;
import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private static final long SEED = 2648718169735535616l;
	private static final int CYCLES = 1_000_000_000;
	private static final int PARSE_INTERVAL = 10_000;

	private static final Chronometer ticksChronometer = new Chronometer();

	public static void main(String... args) {
		RanGen.seed(SEED);
		System.out.println("Seed: " + SEED);
		long startTime = System.currentTimeMillis();

		runExperiment();

		long endTime = System.currentTimeMillis();
		double timeInSeconds = ((double) (endTime - startTime)) / 1000;
		System.out.println("Done (" + timeInSeconds + "s)");
	}

	private static void runExperiment() {
		Pond pond = new Cosmos();
		for (int i = 0; i < CYCLES; i++) {
			pond.tick();
			ticksChronometer.tick();
			if (i % PARSE_INTERVAL == 0)
				System.out.println(getStatusString(pond, i));
		}
	}

	private static String getStatusString(Pond pond, int tick) {
		Narjillo mostProlificNarjillo = pond.getMostProlificNarjillo();
		if (mostProlificNarjillo == null)
			return 	tick + ", " +
					ticksChronometer.getTicksInLastSecond() + ", " +
					pond.getNumberOfNarjillos() + ", " +
					pond.getNumberOfFoodPieces();

		return 	tick + ", " +
				ticksChronometer.getTicksInLastSecond() + ", " +
				pond.getNumberOfNarjillos() + ", " +
				pond.getNumberOfFoodPieces() + ", " +
				mostProlificNarjillo.getNumberOfDescendants() + ", " +
				mostProlificNarjillo.getGenes();
	}
}