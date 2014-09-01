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

import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.utilities.NumberFormat;

public class RunExperiment {

	private static final int PARSE_INTERVAL = 10000;
	private static boolean persistent = false;
	
	// arguments: [<git_commit>, <random_seed | dna_file | dna_document | experiment_file>]
	public static void main(String... args) {
		final Experiment experiment = initializeExperiment(args);

		System.out.println(getHeadersString());
		System.out.println(getStatusString(experiment, 0));

		while (experiment.tick()) {
			if (experiment.getTicksChronometer().getTotalTicks() % PARSE_INTERVAL == 0)
				executePerodicOperations(experiment);
		}
			
		System.out.println("Experiment " + experiment.getId() + " ending at tick " + experiment.getTicksChronometer().getTotalTicks());
	}

	private static void executePerodicOperations(Experiment experiment) {
		long ticks = experiment.getTicksChronometer().getTotalTicks();

		if (ticks % PARSE_INTERVAL != 0)
			return;

		if (persistent)
			writeExperimentToFile(experiment);

		System.out.println(getStatusString(experiment, ticks));
	}

	private static String getHeadersString() {
		return alignLeft("tick")
				+ alignLeft("tps")
				+ alignLeft("narj")
				+ alignLeft("food")
				+ "    most_typical_dna";
	}

	private static String getStatusString(Experiment experiment, long tick) {
		Narjillo mostTypicalSpecimen = getMostTypicalSpecimen(experiment);
		if (mostTypicalSpecimen == null)
			return getStatusString(experiment, tick, "<none>");
		return getStatusString(experiment, tick, mostTypicalSpecimen.getDNA().toString());
	}

	private static String getStatusString(Experiment experiment, long tick, String mostTypicalDNA) {
		return alignLeft(NumberFormat.format(tick))
				+ alignLeft(experiment.getTicksChronometer().getTicksInLastSecond())
				+ alignLeft(experiment.getEcosystem().getNumberOfNarjillos())
				+ alignLeft(experiment.getEcosystem().getNumberOfFoodPieces())
				+ "    " + mostTypicalDNA;
	}

	private static Narjillo getMostTypicalSpecimen(Experiment experiment) {
		return experiment.getEcosystem().getPopulation().getMostTypicalSpecimen();
	}

	private static String alignLeft(Object label) {
		final String padding = "      ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}
	
	public static Experiment initializeExperiment(String... args) {
		String gitCommit = args.length == 0 ? "unknown" : args[0];
		String secondArgument = args.length < 2 ? null : args[1];
		
		final Experiment experiment = createExperiment(gitCommit, secondArgument);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Experiment " + experiment.getId() + " terminated at tick " + experiment.getTicksChronometer().getTotalTicks());
			}
		});
		return experiment;
	}

	private static Experiment createExperiment(String gitCommit, String secondArgument) {
		if (secondArgument == null) {
			System.out.println("New experiment with random seed");
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), null);
		}
		else if (secondArgument.equals("--no-persistence")) {
			System.out.println("New non-persistent experiment with random seed");
			return new Experiment(gitCommit, generateRandomSeed(), null);
		}
		else if (isInteger(secondArgument)) {
			long seed = Long.parseLong(secondArgument);
			System.out.println("New experiment with seed: " + seed);
			persistent = true;
			return new Experiment(gitCommit, seed, null);
		}
		else if (secondArgument.endsWith("nrj")) {
			System.out.println("New experiment populated with DNA from " + secondArgument);
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), readDNAFromFile(secondArgument));
		}
		else if (secondArgument.startsWith("{")) {
			System.out.println("New experiment populated with DNA: " + secondArgument);
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), new DNA(secondArgument));
		}
		else if (secondArgument.endsWith("exp")) {
			System.out.println("Continuing experiment from " + secondArgument);
			persistent = true;
			return readExperimentFromFile(secondArgument);
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

	private static void writeExperimentToFile(Experiment experiment) {
		try {
			String fileName = experiment.getId() + ".exp";
			String tempFileName = fileName + ".tmp";
			
			writeToFile(JSON.toJson(experiment, Experiment.class), tempFileName);
			moveFile(tempFileName, fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void moveFile(String source, String destination) throws IOException {
		Path filePath = Paths.get(destination);
		if (Files.exists(filePath))
			Files.delete(filePath);
		Files.move(Paths.get(source), filePath);
	}

	private static void writeToFile(String content, String fileName) throws IOException, FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		out.print(content);
		out.close();
	}
}
