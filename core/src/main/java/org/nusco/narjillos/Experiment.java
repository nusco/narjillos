package org.nusco.narjillos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.WaterDrop;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.things.Energy;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private static final int PARSE_INTERVAL = 10000;

	private final Ecosystem ecosystem;

	private final Chronometer ticksChronometer = new Chronometer();
	private final long startTime = System.currentTimeMillis();

	// arguments: [<git_commit>, <random_seed | dna_file | dna_document>]
	public Experiment(String... args) {
		LinkedList<String> argumentsList = toList(args);

		String gitCommit = (argumentsList.isEmpty()) ? "UNKNOWN_COMMIT" : argumentsList.removeFirst();
		int seed = extractSeed(argumentsList);
		System.out.println("Experiment " + gitCommit + ":" + seed);
		RanGen.seed(seed);

		ecosystem = extractEcosystem(argumentsList);

		System.out.println(getHeadersString());
	}

	private LinkedList<String> toList(String... args) {
		LinkedList<String> result = new LinkedList<>();
		for (int i = 0; i < args.length; i++)
			result.add(args[i]);
		return result;
	}

	private int extractSeed(LinkedList<String> argumentsList) {
		if (argumentsList.isEmpty() || !isInteger(argumentsList.getFirst())) {
			System.out.print("Randomizing seed... ");
			return Math.abs(new Random().nextInt());
		}

		return Integer.parseInt(argumentsList.removeFirst());
	}

	private Ecosystem extractEcosystem(LinkedList<String> argumentsList) {
		if (argumentsList.isEmpty())
			return new WaterDrop();
		
		if(argumentsList.getFirst().endsWith(".nrj"))
			return new WaterDrop(readDNAFromFile(argumentsList.removeFirst()));
		
		return new WaterDrop(new DNA(argumentsList.removeFirst()));
	}

	private boolean isInteger(String argument) {
		try {
			Integer.parseInt(argument);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static DNA readDNAFromFile(String file) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(file));
			StringBuffer result = new StringBuffer();
			for (String line : lines)
				result.append(line + "\n");
			return new DNA(result.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean tick() {
		reportStatus();
		getEcosystem().tick();
		ticksChronometer.tick();
		return areThereSurvivors();
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public Chronometer getTicksChronometer() {
		return ticksChronometer;
	}

	private void reportStatus() {
		long ticks = ticksChronometer.getTotalTicks();
		
		if (ticks % PARSE_INTERVAL != 0)
			return;

		System.out.println(getStatusString(ecosystem, ticks));
	}

	private long getTimeElapsed() {
		long currentTime = System.currentTimeMillis();
		long timeInSeconds = (currentTime - startTime) / 1000;
		return timeInSeconds;
	}

	private boolean areThereSurvivors() {
		if (getEcosystem().getNumberOfNarjillos() > 0)
			return true;
		
		System.out.println("*** Extinction happens. ***");
		return false;
	}

	private String getHeadersString() {
		return "\n" + alignLeft("ticks") + alignLeft("time") + alignLeft("tps") + alignLeft("narj") + alignLeft("food") + "    most_typical_dna";
	}

	private String getStatusString(Ecosystem ecosystem, long tick) {
		return getStatusString(ecosystem, tick, getMostTypicalSpecimen(ecosystem));
	}

	private Creature getMostTypicalSpecimen(Ecosystem ecosystem) {
		Creature mostTypicalSpecimen = ecosystem.getPopulation().getMostTypicalSpecimen();

		if (mostTypicalSpecimen == null)
			return getNullCreature();
		
		return mostTypicalSpecimen;
	}

	private String getStatusString(Ecosystem ecosystem, long tick, Creature mostTypicalSpecimen) {
		return alignLeft(NumberFormat.format(tick))
				+ alignLeft(NumberFormat.format(getTimeElapsed()))
				+ alignLeft(ticksChronometer.getTicksInLastSecond())
				+ alignLeft(ecosystem.getNumberOfNarjillos())
				+ alignLeft(ecosystem.getNumberOfFoodPieces())
				+ "    " + mostTypicalSpecimen.getDNA();
	}

	private String alignLeft(Object label) {
		final String padding = "        ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}

	private Creature getNullCreature() {
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

			@Override
			public Energy getEnergy() {
				return null;
			}
		};
	}

	public static void main(String... args) {
		final long CYCLES = 100_000_000;

		Experiment experiment = new Experiment(args);
		
		boolean finished = false;
		while (!finished && experiment.getTicksChronometer().getTotalTicks() < CYCLES)
			finished = !experiment.tick();
		
		System.out.println("*** The experiment is over at tick " + experiment.getTicksChronometer().getTotalTicks() +
				" (" + experiment.getTimeElapsed() + "s) ***");
	}
}
