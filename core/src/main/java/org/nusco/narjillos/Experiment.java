package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Drop;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private static final int CYCLES = 100_000_000;
	private static final int PARSE_INTERVAL = 10000;

	private static final Chronometer ticksChronometer = new Chronometer();
	private static long startTime = System.currentTimeMillis();

	public static void main(String... args) {
		String gitCommit = (args.length > 0) ? args[0] : "UNKNOWN_COMMIT";
		int seed = getSeed(args);
		System.out.println("Experiment ID: " + gitCommit + ":" + seed);
		RanGen.seed(seed);
		runExperiment();
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

		Ecosystem pond = new Drop();
		for (long tick = 0; tick < CYCLES; tick++) {
			pond.tick();
			checkForMassExtinction(pond, tick);
			ticksChronometer.tick();

			if (tick > 0 && (tick % PARSE_INTERVAL == 0))
				System.out.println(getStatusString(pond, tick));
		}

		System.out.println("*** The experiment is over at tick " + formatTick(CYCLES) + " (" + getTimeElapsed() + "s) ***");
	}

	private static void checkForMassExtinction(Ecosystem pond, long tick) {
		if (pond.getNumberOfNarjillos() > 0)
			return;
		System.out.println("*** Extinction happens. (tick " + tick + ") ***");
		System.exit(0);
	}

	private static String getHeadersString() {
		return "\ntime, " + "ticks, " + "ticks_per_second, " + "narjillos, " + "food_pieces, " + "most_typical_dna";
	}

	private static String getStatusString(Ecosystem pond, long tick) {
		Creature mostTypicalSpecimen = pond.getPopulation().getMostTypicalSpecimen();
		if (mostTypicalSpecimen == null)
			mostTypicalSpecimen = getNullCreature();

		return getStatusString(pond, tick, mostTypicalSpecimen);
	}

	private static String getStatusString(Ecosystem pond, long tick, Creature mostTypicalSpecimen) {
		return getTimeElapsed() + ", " + tick + ", " + ticksChronometer.getTicksInLastSecond() + ", " + pond.getNumberOfNarjillos() + ", "
				+ pond.getNumberOfFoodPieces() + ", "
				+ mostTypicalSpecimen.getDNA();
	}

	private static String formatTick(long tick) {
		return NumberFormat.format(tick);
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
			}
		};
	}
}
