package org.nusco.narjillos;

import java.util.Random;

import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.ecosystem.Environment;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.serializer.Persistence;
import org.nusco.narjillos.shared.utilities.Configuration;
import org.nusco.narjillos.shared.utilities.NumberFormat;

/**
 * The class that initializes and runs an Experiment. It can be run on its own
 * (the _experiment_ script does just that), or used in a larger system (as the
 * _petri_ script does to provide graphics).
 */
public class ExperimentalLab extends Lab {

	private static boolean persistent = false;
	private final Experiment experiment;
	private volatile boolean isSaving = false;
	private volatile boolean isTerminated = false;
	private volatile long lastSaveTime = System.currentTimeMillis();

	public ExperimentalLab(CommandLineOptions options) {
		String applicationVersion = Persistence.readApplicationVersion();
		
		experiment = createExperiment(applicationVersion, options);
		reportPersistenceOptions(options);
		persistent = options.isPersistent();

		System.out.println(getHeadersString());
		System.out.println(getStatusString(experiment.getTicksChronometer().getTotalTicks()));
	}

	public Environment getEnvironment() {
		return experiment.getEcosystem();
	}

	public boolean tick() {
		if (isTerminated)
			return false;

		boolean thereAreSurvivors = experiment.tick();
		executePeriodOperations();

		if (!thereAreSurvivors)
			System.out.println("*** Extinction happens. ***");

		return thereAreSurvivors;
	}

	public boolean isBusy() {
		return isSaving;
	}

	@Override
	public void terminate() {
		while (isBusy()) {
			sleepAWhile();
		}
		isTerminated = true;
		String finalReport = experiment.terminate();
		System.out.println(finalReport);
	}

	private void sleepAWhile() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private Experiment createExperiment(String applicationVersion, CommandLineOptions options) {
		Experiment result;
		
		Ecosystem ecosystem = new Ecosystem(Configuration.ECOSYSTEM_SIZE, true);
		
		String dna = options.getDna();
		boolean trackingGenePool = options.isTrackingGenePool();
		if (dna != null) {
			System.out.print("Observing DNA " + dna);
			result = new Experiment(generateRandomSeed(), ecosystem, applicationVersion, trackingGenePool, dna);
		} else if (options.getExperiment() != null) {
			System.out.print("Continuining experiment " + options.getExperiment().getId());
			result = options.getExperiment();
		} else if (options.getSeed() == CommandLineOptions.NO_SEED) {
			long randomSeed = generateRandomSeed();
			System.out.print("Starting new experiment with random seed: " + randomSeed);
			result = new Experiment(randomSeed, ecosystem, applicationVersion, trackingGenePool);
		} else {
			System.out.print("Starting experiment " + options.getSeed());
			result = new Experiment(options.getSeed(), ecosystem, applicationVersion, trackingGenePool);
		}
		
		return result;
	}

	private void reportPersistenceOptions(CommandLineOptions options) {
		if (options.isPersistent() && options.isTrackingGenePool())
			System.out.println(" (persisted to file with ancestry)");
		else if (options.isPersistent())
			System.out.println(" (persisted to file)");
		else
			System.out.println(" (no persistence)");
	}

	private void executePeriodOperations() {
		long ticks = experiment.getTicksChronometer().getTotalTicks();

		if (ticks % Configuration.EXPERIMENT_SAMPLE_INTERVAL_TICKS != 0)
			return;

		System.out.println(getStatusString(ticks));

		if (!persistent)
			return;
		
		if ((System.currentTimeMillis() - lastSaveTime) / 1000.0 > Configuration.EXPERIMENT_SAVE_INTERVAL_SECONDS) {
			save();
			lastSaveTime = System.currentTimeMillis();
		}
	}

	private void save() {
		isSaving = true;
		System.out.println("> Saving...");
		Persistence.save(experiment);
		isSaving = false;
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

	private long generateRandomSeed() {
		return Math.abs(new Random().nextInt());
	}

	public static void main(String... args) {
		CommandLineOptions options = CommandLineOptions.parse(args);
		if (options == null)
			System.exit(1);
		
		final ExperimentalLab lab = new ExperimentalLab(options);

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

	@Override
	public String getPerformanceStatistics() {
		return "FPS: " + getTicksInLastSecond() + " / TPS: " + getTicksInLastSecond() + " / Ticks: "
				+ NumberFormat.format(getTotalTicks());
	}

	private int getTicksInLastSecond() {
		return experiment.getTicksChronometer().getTicksInLastSecond();
	}

	private long getTotalTicks() {
		return experiment.getTicksChronometer().getTotalTicks();
	}
}
