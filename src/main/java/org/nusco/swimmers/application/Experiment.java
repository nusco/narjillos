package org.nusco.swimmers.application;

import java.util.Comparator;
import java.util.List;

import org.nusco.swimmers.creature.Narjillo;
import org.nusco.swimmers.pond.Pond;
import org.nusco.swimmers.shared.utilities.Chronometer;
import org.nusco.swimmers.shared.utilities.RanGen;

public class Experiment {

	private static final int CYCLES = 100_000_000;
	private static final long SEED = 2648718169735535616l;

	private static final Chronometer ticksChronometer = new Chronometer();

	public static void main(String... args) {
		RanGen.seed(SEED);

		System.out.println("Starting...");
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
			if (i % 10_000 == 0)
				System.out.println(getStatusString(pond, i));
		}
	}

	private static Narjillo getMostProlificNarjillo(Pond pond) {
		List<Narjillo> narjillos = pond.getNarjillos();
		narjillos.sort(new Comparator<Narjillo>() {
			@Override
			public int compare(Narjillo n1, Narjillo n2) {
				return n2.getNumberOfDescendants() - n1.getNumberOfDescendants();
			}
		});
		return narjillos.get(0);
	}

	private static String getStatusString(Pond pond, int tick) {
		Narjillo mostProlificNarjillo = getMostProlificNarjillo(pond);

		return 	mostProlificNarjillo.getNumberOfDescendants() + ", " +
				mostProlificNarjillo.getGenes() + ", " +
				tick + ", " +
				pond.getNumberOfNarjillos() + ", " +
				pond.getNumberOfFoodPieces() + ", " +
				ticksChronometer.getTicksInLastSecond();
	}
}