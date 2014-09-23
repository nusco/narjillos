package org.nusco.narjillos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.JSON;
import org.nusco.narjillos.shared.utilities.NumberFormat;

public class Lab {

	private static final int PARSE_INTERVAL = 10000;
	private static boolean persistent = false;
	private final Experiment experiment;
	private GenePool genePool = new GenePool();

	public Lab(String... args) {
		DNA.setObserver(genePool);
		experiment = initialize(args);
		System.out.println(getHeadersString());
		System.out.println(getStatusString(experiment.getTicksChronometer().getTotalTicks()));
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public Ecosystem getEcosystem() {
		return experiment.getEcosystem();
	}

	public int getTicksInLastSecond() {
		return experiment.getTicksChronometer().getTicksInLastSecond();
	}

	public long getTotalTicks() {
		return experiment.getTicksChronometer().getTotalTicks();
	}

	public boolean tick() {
		boolean thereAreSurvivors = experiment.tick();
		if (experiment.getTicksChronometer().getTotalTicks() % PARSE_INTERVAL == 0)
			executePerodicOperations();

		if (!thereAreSurvivors) {
			System.out.println("*** Extinction happens. ***");
			reportEndOfExperiment();
		}

		return thereAreSurvivors;
	}

	private void executePerodicOperations() {
		long ticks = experiment.getTicksChronometer().getTotalTicks();

		if (ticks % PARSE_INTERVAL != 0)
			return;

		if (persistent)
			writeStatusFiles();

		System.out.println(getStatusString(ticks));
	}

	private void writeStatusFiles() {
		String experimentFileName = experiment.getId() + ".exp";
		String tempExperimentFileName = experimentFileName + ".tmp";
		
		String ancestryFileName = experiment.getId() + ".gpl";
		String tempAncestryFileName = ancestryFileName + ".tmp";

		try {
			writeToFile(tempExperimentFileName, JSON.toJson(experiment, Experiment.class));
			writeToFile(tempAncestryFileName, JSON.toJson(genePool, GenePool.class));

			// do this quickly to minimize the chances of getting
			// the two files misaligned
			moveFile(tempExperimentFileName, experimentFileName);
			moveFile(tempAncestryFileName, ancestryFileName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getHeadersString() {
		return alignLeft("tick") + alignLeft("time") + alignLeft("tps") + alignLeft("narj") + alignLeft("food");
	}

	private String getStatusString(long tick) {
		return alignLeft(NumberFormat.format(tick))
				+ alignLeft(NumberFormat.format(experiment.getTotalRunningTimeInSeconds()))
				+ alignLeft(experiment.getTicksChronometer().getTicksInLastSecond())
				+ alignLeft(experiment.getEcosystem().getNumberOfNarjillos())
				+ alignLeft(experiment.getEcosystem().getNumberOfFoodPieces());
	}

	private String alignLeft(Object label) {
		final String padding = "        ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}

	public Experiment initialize(String... args) {
		String gitCommit = args.length == 0 ? "unknown" : args[0];
		String secondArgument = args.length < 2 ? null : args[1];

		final Experiment experiment = createExperiment(gitCommit, secondArgument);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				experiment.stop();
				reportEndOfExperiment();
			}
		});

		DNA.setObserver(genePool);

		return experiment;
	}

	private Experiment createExperiment(String gitCommit, String secondArgument) {
		if (secondArgument == null) {
			System.out.println("Starting new experiment with random seed");
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), null);
		} else if (secondArgument.equals("no-persistence")) {
			System.out.println("Starting new non-persistent experiment with random seed");
			return new Experiment(gitCommit, generateRandomSeed(), null);
		} else if (isInteger(secondArgument)) {
			long seed = Long.parseLong(secondArgument);
			System.out.println("Starting experiment " + seed + " from scratch");
			persistent = true;
			return new Experiment(gitCommit, seed, null);
		} else if (secondArgument.endsWith(".nrj")) {
			DNA dna = readDNAFromFile(secondArgument);
			System.out.println("Observing DNA " + dna);
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), dna);
		} else if (secondArgument.startsWith("{")) {
			System.out.println("Observing DNA " + secondArgument);
			persistent = true;
			return new Experiment(gitCommit, generateRandomSeed(), new DNA(secondArgument));
		} else {
			String experimentId = stripFileExtension(secondArgument);
			System.out.println("Picking up experiment from " + experimentId + ".exp");
			persistent = true;
			genePool = readGenePoolFromFile(experimentId);
			return readExperimentFromFile(experimentId);
		}
	}

	private String stripFileExtension(String fileName) {
		int extensionIndex = fileName.lastIndexOf('.');
		return (extensionIndex < 0) ? fileName : fileName.substring(0, extensionIndex);
	}

	private long generateRandomSeed() {
		return Math.abs(new Random().nextInt());
	}

	private boolean isInteger(String argument) {
		try {
			Integer.parseInt(argument);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private DNA readDNAFromFile(String file) {
		return new DNA(read(file));
	}

	private Experiment readExperimentFromFile(String file) {
		Experiment result = JSON.fromJson(read(file + ".exp"), Experiment.class);
		result.timeStamp();
		return result;
	}

	private GenePool readGenePoolFromFile(String file) {
		Path filePath = Paths.get(file + ".gpl");
		if (!Files.exists(filePath)) {
			System.out.println("Warning: no .gpl file found. Continuing with empty genepool.");
			return new GenePool();
		}
		return JSON.fromJson(read(file + ".gpl"), GenePool.class);
	}

	private String read(String file) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(file));
			return new String(encoded, Charset.defaultCharset());
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

	private void writeToFile(String fileName, String content) throws IOException, FileNotFoundException {
		PrintWriter out = new PrintWriter(fileName);
		out.print(content);
		out.close();
	}

	private void reportEndOfExperiment() {
		System.out.println("Experiment " + experiment.getId() + " ending at " + experiment.getTotalRunningTimeInSeconds() + " seconds, "
				+ experiment.getTicksChronometer().getTotalTicks() + " ticks");
	}

	// arguments: [<git_commit>, <random_seed | dna_file | dna_document |
	// experiment_file>]
	public static void main(String... args) {
		Lab lab = new Lab(args);
		while (lab.tick());
	}
}
