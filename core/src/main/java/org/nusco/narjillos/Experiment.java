package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.pond.Cosmos;
import org.nusco.narjillos.pond.Pond;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private static final int CYCLES = 100_000_000;
	private static final int PARSE_INTERVAL = 10000;

	private static final Chronometer ticksChronometer = new Chronometer();
	private static long startTime = System.currentTimeMillis();

	public static void main(String... args) {
		String gitCommit = (args.length > 0) ? args[0] : "UNKNOWN_COMMIT";
		int seed = getSeed(args);
		System.out.print("Experiment ID: " + gitCommit + ":" + seed);

		RanGen.seed(seed);
		runExperiment();

		System.out.println("*** The experiment is over (" + getTimeElapsed() + "s) ***");
	}

	private static int getSeed(String... args) {
		if (args.length < 2)
			return Math.abs(new Random().nextInt());
		return Integer.parseInt(args[1]);
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
		return "\ntime_elapsed, " + "ticks_elapsed, " + "ticks_per_second, " + "number_of_narjillos, " + "number_of_food_pieces, " + "most_typical_specimen";
	}

	private static String getStatusString(Pond pond, int tick) {
		Creature mostTypicalSpecimen = pond.getPopulation().getMostTypicalSpecimen();
		if (mostTypicalSpecimen == null)
			mostTypicalSpecimen = getNullCreature();

		return getStatusString(pond, tick, mostTypicalSpecimen);
	}

	private static Creature getNullCreature() {
		return new Creature() {

			@Override
			public void tick() {
			}

			@Override
			public DNA getDNA() {
				return new DNA("{0_0_0_0}");
			}

			@Override
			public Vector getPosition() {
				return Vector.ZERO;
			}

			@Override
			public String getLabel() {
				return "nobody";
			}};
	}

	private static String getStatusString(Pond pond, int tick, Creature mostTypicalSpecimen) {
		return getTimeElapsed() + ", " + tick + ", " + ticksChronometer.getTicksInLastSecond() + ", " + pond.getNumberOfNarjillos() + ", "
				+ pond.getNumberOfFoodPieces() + ", "
				+ mostTypicalSpecimen.getDNA();
	}
}