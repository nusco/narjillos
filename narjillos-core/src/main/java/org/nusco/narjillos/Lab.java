package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
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
	private volatile boolean isSaving = false;
	private volatile boolean isTerminated = false;

	public Lab(String applicationVersion, String... args) {
		experiment = initialize(applicationVersion, args);
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
		if (isTerminated)
			return false;
		
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
			Persistence.save(experiment);
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

	private Experiment initialize(String applicationVersion, String... args) {
		// TODO: real argument parsing
		String argument = args.length == 0 ? null : args[0];
		return createExperiment(applicationVersion, argument);
	}

	private Experiment createExperiment(String applicationVersion, String argument) {
		// TODO: replace with real argument parsing
		if (argument == null) {
			System.out.println("Starting new experiment with random seed");
			persistent = true;
			Experiment result = new Experiment(generateRandomSeed(), applicationVersion, null);
			System.out.println(result);
			return result;
		} else if (argument.equals("no-persistence")) {
			System.out.println("Starting new non-persistent experiment with random seed");
			Experiment result = new Experiment(generateRandomSeed(), applicationVersion, null);
			return result;
		} else if (isInteger(argument)) {
			long seed = Long.parseLong(argument);
			System.out.println("Starting experiment " + seed + " from scratch");
			persistent = true;
			Experiment result = new Experiment(seed, applicationVersion, null);
			return result;
		} else if (argument.endsWith(".dna")) {
			DNA dna = Persistence.loadDNA(argument);
			System.out.println("Observing DNA " + dna);
			persistent = true;
			Experiment result = new Experiment(generateRandomSeed(), applicationVersion, dna);
			return result;
		} else if (argument.startsWith("{")) {
			System.out.println("Observing DNA " + argument);
			persistent = true;
			Experiment result = new Experiment(generateRandomSeed(), applicationVersion, new DNA(argument));
			return result;
		} else {
			System.out.println("Picking up experiment from " + argument);
			persistent = true;
			return Persistence.loadExperiment(argument);
		}
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
		isTerminated = true;
		String finalReport = experiment.terminate();
		System.out.println(finalReport);
	}

	// arguments: [<random_seed | dna_file | dna_document | experiment_file>]
	public static void main(String... args) {
		final Lab lab = new Lab(Persistence.readApplicationVersion(), args);

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
