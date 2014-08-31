package org.nusco.narjillos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.nusco.narjillos.creature.genetics.Creature;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.NumberFormat;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private final static int ECOSYSTEM_SIZE = 40_000;
	private static final int INITIAL_NUMBER_OF_FOOD_PIECES = 400;
	private static final int MAX_NUMBER_OF_FOOD_PIECES = 600;
	private static final int FOOD_RESPAWN_AVERAGE_INTERVAL = 100;
	private static final int INITIAL_NUMBER_OF_NARJILLOS = 300;
	private static final int PARSE_INTERVAL = 10000;

	private final String id;
	private final Ecosystem ecosystem;
	private final Chronometer ticksChronometer = new Chronometer();
	private final long startTime = System.currentTimeMillis();
	private final RanGen ranGen;
	private transient boolean persistent = false;
	
	public Experiment(String gitCommit, long seed) {
		this(gitCommit, seed, null);
	}

	public Experiment(String gitCommit, long seed, DNA dna) {
		id = gitCommit + "-" + seed;
		ranGen = new RanGen(seed);
		ecosystem = new Ecosystem(ECOSYSTEM_SIZE);
		System.out.println(getHeadersString());
		populate(ecosystem, dna);
	}

	public String getId() {
		return id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setPersistent() {
		persistent = true;
	}
	
	private void populate(Ecosystem ecosystem, DNA dna) {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_PIECES; i++)
			ecosystem.spawnFood(randomPosition(ecosystem.getSize()));

		for (int i = 0; i < INITIAL_NUMBER_OF_NARJILLOS; i++) {
			if (dna == null)
				ecosystem.spawnNarjillo(randomPosition(ecosystem.getSize()), DNA.random(ranGen));
			else
				ecosystem.spawnNarjillo(randomPosition(ecosystem.getSize()), dna);
		}
	}

	private Vector randomPosition(long size) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	public boolean tick() {
		boolean running = getEcosystem().tick(ranGen);
		if (running) {
			ticksChronometer.tick();
			if (shouldSpawnFood())
				ecosystem.spawnFood(randomPosition(ecosystem.getSize()));
		}
		executePerodicOperations();
		return areThereSurvivors();
	}

	private boolean shouldSpawnFood() {
		return ecosystem.getNumberOfFoodPieces() < MAX_NUMBER_OF_FOOD_PIECES &&
				ranGen.nextDouble() < 1.0 / FOOD_RESPAWN_AVERAGE_INTERVAL;
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public Chronometer getTicksChronometer() {
		return ticksChronometer;
	}

	private void executePerodicOperations() {
		long ticks = ticksChronometer.getTotalTicks();

		if (ticks % PARSE_INTERVAL != 0)
			return;

		if (persistent)
			writeExperimentToFile();

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
		return "\n" + alignLeft("id") + alignLeft("tick") + alignLeft("time") + alignLeft("tps") + alignLeft("narj") + alignLeft("food")
				+ "    most_typical_dna";
	}

	private String getStatusString(Ecosystem ecosystem, long tick) {
		Creature mostTypicalSpecimen = getMostTypicalSpecimen(ecosystem);
		if (mostTypicalSpecimen == null)
			return getStatusString(ecosystem, tick, "<none>");
		return getStatusString(ecosystem, tick, mostTypicalSpecimen.getDNA().toString());
	}

	private String getStatusString(Ecosystem ecosystem, long tick, String mostTypicalDNA) {
		return alignLeft(id) + alignLeft(NumberFormat.format(tick)) + alignLeft(NumberFormat.format(getTimeElapsed()))
				+ alignLeft(ticksChronometer.getTicksInLastSecond()) + alignLeft(ecosystem.getNumberOfNarjillos())
				+ alignLeft(ecosystem.getNumberOfFoodPieces()) + "    " + mostTypicalDNA;
	}

	private Creature getMostTypicalSpecimen(Ecosystem ecosystem) {
		return ecosystem.getPopulation().getMostTypicalSpecimen();
	}

	private String alignLeft(Object label) {
		final String padding = "                  ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}

	// arguments: [<git_commit>, <random_seed | dna_file | dna_document | experiment_file>]
	public static void main(String... args) {
		Experiment experiment = initializeExperiment(args);

		boolean finished = false;
		while (!finished)
			finished = !experiment.tick();

		System.out.println("*** Experiment " + experiment.getId() + " is over at tick " + experiment.getTicksChronometer().getTotalTicks() + " ("
				+ experiment.getTimeElapsed() + "s) ***");
	}
	
	public static Experiment initializeExperiment(String... args) {
		String gitCommit = args.length == 0 ? "unknown" : args[0];
		String secondArgument = args.length < 2 ? null : args[1];
		
		return createExperiment(gitCommit, secondArgument);
	}

	private static Experiment createExperiment(String gitCommit, String secondArgument) {
		if (secondArgument == null) {
			System.out.println("New experiment with random seed");
			Experiment result = new Experiment(gitCommit, generateRandomSeed(), null);
			result.setPersistent();
			return result;
		}
		else if (secondArgument.equals("--no-persistence")) {
			System.out.println("New non-persistent experiment with random seed");
			return new Experiment(gitCommit, generateRandomSeed(), null);
		}
		else if (isInteger(secondArgument)) {
			long seed = Long.parseLong(secondArgument);
			System.out.println("New experiment with seed: " + seed);
			Experiment result = new Experiment(gitCommit, seed, null);
			result.setPersistent();
			return result;
		}
		else if (secondArgument.endsWith("nrj")) {
			System.out.println("New experiment populated with DNA from " + secondArgument);
			Experiment result = new Experiment(gitCommit, generateRandomSeed(), readDNAFromFile(secondArgument));
			result.setPersistent();
			return result;
		}
		else if (secondArgument.startsWith("{")) {
			System.out.println("New experiment populated with DNA: " + secondArgument);
			Experiment result = new Experiment(gitCommit, generateRandomSeed(), new DNA(secondArgument));
			result.setPersistent();
			return result;
		}
		else if (secondArgument.endsWith("exp")) {
			System.out.println("Continuing experiment from " + secondArgument);
			Experiment result = readExperimentFromFile(secondArgument);
			result.setPersistent();
			return result;
		}
		
		throw new RuntimeException("Invalid experiment arguments. Use: 	[<git_commit>, <random_seed | dna_file | dna_document | experiment_file>]");
	}

	private static long generateRandomSeed() {
		return Math.abs(new Random().nextInt());
	}

	private static boolean isInteger(String argument) {
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

	private static Experiment readExperimentFromFile(String file) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(file));
			String json = new String(encoded, Charset.defaultCharset());
			return JSON.fromJson(json, Experiment.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeExperimentToFile() {
		try {
			String fileName = getId() + ".exp";
			String tempFileName = fileName + ".tmp";
			
			writeToFile(JSON.toJson(this, Experiment.class), tempFileName);
			moveFile(tempFileName, fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void moveFile(String source, String destination) throws IOException {
		Path filePath = Paths.get(destination);
		if (Files.exists(filePath))
			Files.delete(filePath);
		Files.move(Paths.get(source), filePath);
	}

	private void writeToFile(String content, String fileName) throws IOException, FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		out.print(content);
		out.close();
	}
}
