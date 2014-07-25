package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.body.Head;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.pond.Cosmos;
import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.ColorByte;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private static final int CYCLES = 100_000_000;
	private static final int PARSE_INTERVAL = 1000;

	private static final Chronometer ticksChronometer = new Chronometer();
	private static long startTime = System.currentTimeMillis();

	public static void main(String... args) {
		seedRandomGenerator(args);
		runExperiment();
		System.out.println("Done (" + getTimeElapsed() + "s)");
	}

	private static void seedRandomGenerator(String... args) {
		long seed = getSeed(args);
		System.out.println("Seed: " + seed);
		RanGen.seed(seed);
	}

	private static long getSeed(String... args) {
		if (args.length == 0)
			return Math.abs(new Random().nextLong());
		return Long.parseLong(args[0]);
	}

	private static long getTimeElapsed() {
		long currentTime = System.currentTimeMillis();
		long timeInSeconds = (currentTime - startTime) / 1000;
		return timeInSeconds;
	}

	private static void runExperiment() {
		System.out.println(getHeadersString());

		Pond pond = new Cosmos();
		for (int i = 0; i < CYCLES; i++) {
			pond.tick();
			ticksChronometer.tick();

			if (i > 0 && (i % PARSE_INTERVAL == 0))
				System.out.println(getStatusString(pond, i));
		}
	}

	private static String getHeadersString() {
		return "time_elapsed, " + "ticks_elapsed, " + "ticks_per_second, " + "number_of_narjillos, " + "number_of_food_pieces, "
				+ "most_prolific_narjillo_number_of_descendants, " + "most_prolific_narjillo_dna";
	}

	private static String getStatusString(Pond pond, int tick) {
		Narjillo mostProlificNarjillo = pond.getMostProlificNarjillo();
		if (mostProlificNarjillo == null)
			mostProlificNarjillo = getNullNarjillo();

		return getStatusString(pond, tick, mostProlificNarjillo);
	}

	private static Narjillo getNullNarjillo() {
		return new Narjillo(new Head(0, 0, new ColorByte(0), 0), new DNA(new Integer[0]));
	}

	private static String getStatusString(Pond pond, int tick, Narjillo mostProlificNarjillo) {
		return getTimeElapsed() + ", " + tick + ", " + ticksChronometer.getTicksInLastSecond() + ", " + pond.getNumberOfNarjillos() + ", "
				+ pond.getNumberOfFoodPieces() + ", " + mostProlificNarjillo.getNumberOfDescendants() + ", "
				+ mostProlificNarjillo.getGenes();
	}
}