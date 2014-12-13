package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.serializer.Persistence;
import org.nusco.narjillos.shared.utilities.NumberFormat;

/**
 * The class that initialized and runs an Experiment. It can be run on its own
 * (the _experiment_ script does just that), or used in a larger system (as the
 * _petridish_ script does to provide graphics).
 */
public class Lab {

	private static final int PARSE_INTERVAL = 10000;
	private static boolean persistent = false;
	private final Experiment experiment;
	private GenePool genePool = new GenePool();
	private volatile boolean isSaving = false;

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

		if (!thereAreSurvivors)
			System.out.println("*** Extinction happens. ***");

		return thereAreSurvivors;
	}

	private void executePerodicOperations() {
		long ticks = experiment.getTicksChronometer().getTotalTicks();

		if (ticks % PARSE_INTERVAL != 0)
			return;

		if (persistent) {
			isSaving = true;
			Persistence.save(experiment, genePool);
			isSaving = false;
		}

		System.out.println(getStatusString(ticks));
	}

	private String getHeadersString() {
		return alignLeft("tick") + alignLeft("time") + alignLeft("eggs") + alignLeft("narj") + alignLeft("food");
	}

	private String getStatusString(long tick) {
		return alignLeft(NumberFormat.format(tick)) + alignLeft(NumberFormat.format(experiment.getTotalRunningTimeInSeconds()))
				+ alignLeft(experiment.getEcosystem().getNumberOfEggs()) + alignLeft(experiment.getEcosystem().getNumberOfNarjillos())
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

		Experiment experiment = createExperiment(gitCommit, secondArgument);
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
			DNA dna = Persistence.loadDNA(secondArgument);
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
			genePool = Persistence.loadGenePool(experimentId);
			return Persistence.loadExperiment(experimentId);
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

	public boolean isSaving() {
		return isSaving;
	}

	protected void terminate() {
		while(isSaving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		String finalReport = experiment.terminate();
		System.out.println(finalReport);
	}

	// arguments: [<git_commit>, <random_seed | dna_file | dna_document |
	// experiment_file>]
	public static void main(String... args) {
		final Lab lab = new Lab(args);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				lab.terminate();
			}
		});

		while (lab.tick())
			;
		System.exit(0);
	}
}
