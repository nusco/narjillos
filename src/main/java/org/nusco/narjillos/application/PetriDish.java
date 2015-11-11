package org.nusco.narjillos.application;

import java.util.Random;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumberFormatter;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.experiment.VolatileHistoryLog;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.Environment;
import org.nusco.narjillos.genomics.DNALog;
import org.nusco.narjillos.genomics.VolatileDNALog;
import org.nusco.narjillos.persistence.ExperimentLog;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;

/**
 * The class that initializes and runs an Experiment.
 */
public class PetriDish implements Dish {

	private static boolean persistent = false;
	private final Experiment experiment;
	private final ExperimentLog experimentLog;
	private volatile boolean isSaving = false;
	private volatile boolean isTerminated = false;
	private volatile long lastSaveTime = System.currentTimeMillis();

	public PetriDish(String version, CommandLineOptions options, int size) {
		experiment = createExperiment(version, options, size);
		experimentLog = new ExperimentLog(experiment.getId());
		if (isNewExperiment(experiment))
			experimentLog.save(experiment);
		reportPersistenceOptions(options);
		persistent = options.isPersistent();
		
		System.out.println("Ticks:\tNarji:\tFood:");
	}

	private boolean isNewExperiment(Experiment experiment) {
		return experiment.getTicksChronometer().getTotalTicks() == 0;
	}

	public Environment getEnvironment() {
		return experiment.getEcosystem();
	}

	public boolean tick() {
		if (isTerminated)
			return false;

		executePeriodOperations();
		experiment.tick();
		return true;
	}

	public boolean isBusy() {
		return isSaving;
	}

	public void terminate() {
		while (isBusy())
			sleepAWhile();
		String finalReport = experiment.terminate();
		System.out.println(finalReport);
		isTerminated = true;
	}

	private void sleepAWhile() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private Experiment createExperiment(String applicationVersion, CommandLineOptions options, int size) {
		Ecosystem ecosystem = new Ecosystem(size, true);
		String dna = options.getDna();

		Experiment experiment;
		if (dna != null) {
			System.out.print("Observing DNA " + dna);
			experiment = new Experiment(generateRandomSeed(), ecosystem, applicationVersion);
			setPersistenceStrategies(experiment, options);
			experiment.populate(dna);
		} else if (options.getExperiment() != null) {
			System.out.print("Continuining experiment " + options.getExperiment().getId());
			experiment = options.getExperiment();
			setPersistenceStrategies(experiment, options);
			return experiment;
		} else if (options.getSeed() == CommandLineOptions.NO_SEED) {
			long randomSeed = generateRandomSeed();
			System.out.print("Starting new experiment with random seed: " + randomSeed);
			experiment = new Experiment(randomSeed, ecosystem, applicationVersion);
			setPersistenceStrategies(experiment, options);
			experiment.populate();
		} else {
			System.out.print("Starting experiment " + options.getSeed());
			experiment = new Experiment(options.getSeed(), ecosystem, applicationVersion);
			setPersistenceStrategies(experiment, options);
			experiment.populate();
		}
		return experiment;
	}

	private void setPersistenceStrategies(Experiment experiment, CommandLineOptions options) {
		if (options.isPersistent())
			setPersistenceStrategies(experiment, new PersistentDNALog(experiment.getId()), new PersistentHistoryLog(experiment.getId()));
		else
			setPersistenceStrategies(experiment, new VolatileDNALog(), new VolatileHistoryLog());
	}

	private void setPersistenceStrategies(Experiment result, DNALog dnaLog, HistoryLog historyLog) {
		result.setDnaLog(dnaLog);
		result.setHistoryLog(historyLog);
	}

	private void reportPersistenceOptions(CommandLineOptions options) {
		if (options.isPersistent())
			System.out.println(" (persisted to file)");
		else
			System.out.println(" (no persistence)");
	}

	private void executePeriodOperations() {
		long ticks = experiment.getTicksChronometer().getTotalTicks();
		if (ticks % Configuration.EXPERIMENT_SAMPLE_INTERVAL_TICKS != 0)
			return;

		experiment.saveHistoryEntry();
		System.out.println(getReport());
		
		if (!experiment.thereAreSurvivors()) {
			// extinction!
			isTerminated = true;
			if (persistent)
				save();
		} else if (persistent) {
			double secondsSinceLastSave = (System.currentTimeMillis() - lastSaveTime) / 1000.0;
			if (secondsSinceLastSave > Configuration.EXPERIMENT_SAVE_INTERVAL_SECONDS) {
				save();
				lastSaveTime = System.currentTimeMillis();
			}
		}
	}

	private String getReport() {
		return 	NumberFormatter.format(experiment.getTicksChronometer().getTotalTicks()) + "\t" +
				experiment.getEcosystem().getNumberOfNarjillos() + "\t" +
				experiment.getEcosystem().getNumberOfFoodPellets();
	}

	private void save() {
		isSaving = true;
		System.out.print("> Saving...");
		experimentLog.save(experiment);
		System.out.println(" Done.");
		isSaving = false;
	}

	private long generateRandomSeed() {
		return Math.abs(new Random().nextInt() % 1_000_000_000);
	}

	public String getStatistics() {
		return "TPS: " + getTicksInLastSecond() + " / Ticks: " + NumberFormatter.format(getTotalTicks());
	}

	private int getTicksInLastSecond() {
		return experiment.getTicksChronometer().getTicksInLastSecond();
	}

	private long getTotalTicks() {
		return experiment.getTicksChronometer().getTotalTicks();
	}
}
