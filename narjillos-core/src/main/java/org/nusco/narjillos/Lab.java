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
 * _petri_ script does to provide graphics).
 */
public class Lab {

	private static final int PARSE_INTERVAL = 10000;
	private static boolean persistent = false;
	private final Experiment experiment;
	private volatile boolean isSaving = false;
	private volatile boolean isTerminated = false;

	public Lab(CommandLineOptions options) {
		String applicationVersion = Persistence.readApplicationVersion();
		
		experiment = createExperiment(applicationVersion, options);
		reportPersistenceOptions(options);
		persistent = options.isPersistent();

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

	public boolean isSaving() {
		return isSaving;
	}

	protected void terminate() {
		while (isSaving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		isTerminated = true;
		String finalReport = experiment.terminate();
		System.out.println(finalReport);
	}

	private Experiment createExperiment(String applicationVersion, CommandLineOptions options) {
		Experiment result;
		
		DNA dna = options.getDna();
		boolean trackingGenePool = options.isTrackingGenePool();
		if (dna != null) {
			System.out.print("Observing DNA " + dna);
			result = new Experiment(generateRandomSeed(), applicationVersion, dna, trackingGenePool);
		} else if (options.getExperiment() != null) {
			System.out.print("Continuining experiment " + options.getExperiment().getId());
			result = options.getExperiment();
		} else if (options.getSeed() == CommandLineOptions.NO_SEED) {
			long randomSeed = generateRandomSeed();
			System.out.print("Starting new experiment with random seed: " + randomSeed);
			result = new Experiment(randomSeed, applicationVersion, null, trackingGenePool);
		} else {
			System.out.print("Starting experiment " + options.getSeed());
			result = new Experiment(options.getSeed(), applicationVersion, null, trackingGenePool);
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

	private long generateRandomSeed() {
		return Math.abs(new Random().nextInt());
	}

	public static void main(String... args) {
		CommandLineOptions options = CommandLineOptions.parse(args);
		if (options == null)
			System.exit(1);
		
		final Lab lab = new Lab(options);

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
