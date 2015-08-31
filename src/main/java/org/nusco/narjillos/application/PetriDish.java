package org.nusco.narjillos.application;

import java.util.Random;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.Environment;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolWithHistory;
import org.nusco.narjillos.genomics.SimpleGenePool;
import org.nusco.narjillos.persistence.PersistentDNALog;
import org.nusco.narjillos.persistence.PersistentHistoryLog;
import org.nusco.narjillos.persistence.VolatileHistoryLog;
import org.nusco.narjillos.persistence.serialization.FilePersistence;

/**
 * The class that initializes and runs an Experiment.
 */
public class PetriDish implements Dish {

	private static boolean persistent = false;
	private final Experiment experiment;
	private volatile boolean isSaving = false;
	private volatile boolean isTerminated = false;
	private volatile long lastSaveTime = System.currentTimeMillis();

	public PetriDish(String version, CommandLineOptions options, int size) {
		experiment = createExperiment(version, options, size);
		reportPersistenceOptions(options);
		persistent = options.isPersistent();
		
		System.out.println("Ticks:\tNarji:\tFood:");
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
		Experiment result;
		
		Ecosystem ecosystem = new Ecosystem(size, true);
		
		String dna = options.getDna();
		if (dna != null) {
			System.out.print("Observing DNA " + dna);
			result = new Experiment(generateRandomSeed(), ecosystem, applicationVersion, dna);
		} else if (options.getExperiment() != null) {
			System.out.print("Continuining experiment " + options.getExperiment().getId());
			return options.getExperiment();
		} else if (options.getSeed() == CommandLineOptions.NO_SEED) {
			long randomSeed = generateRandomSeed();
			System.out.print("Starting new experiment with random seed: " + randomSeed);
			result = new Experiment(randomSeed, ecosystem, applicationVersion);
		} else {
			System.out.print("Starting experiment " + options.getSeed());
			result = new Experiment(options.getSeed(), ecosystem, applicationVersion);
		}

		if (options.isKeepingHistory())
			setPersistenceStrategies(result, new GenePoolWithHistory(new PersistentDNALog(result.getId())), new PersistentHistoryLog(result.getId()));
		else
			setPersistenceStrategies(result, new SimpleGenePool(), new VolatileHistoryLog());
		return result;
	}

	private void setPersistenceStrategies(Experiment result, GenePool genePool, HistoryLog historyLog) {
		result.setGenePool(genePool);
		result.setHistoryLog(historyLog);
	}

	private void reportPersistenceOptions(CommandLineOptions options) {
		if (options.isPersistent() && options.isKeepingHistory())
			System.out.println(" (persisted to file and database)");
		else if (options.isPersistent())
			System.out.println(" (persisted to file, no database)");
		else
			System.out.println(" (no persistence)");
	}

	private void executePeriodOperations() {
		long ticks = experiment.getTicksChronometer().getTotalTicks();
		if (ticks % Configuration.EXPERIMENT_SAMPLE_INTERVAL_TICKS != 0)
			return;

		if (!experiment.thereAreSurvivors())
			isTerminated = true;

		experiment.saveHistoryEntry();
		System.out.println(getReport());
		
		if (persistent)
			saveExperimentToFile();
	}

	private void saveExperimentToFile() {
		if ((System.currentTimeMillis() - lastSaveTime) / 1000.0 > Configuration.EXPERIMENT_SAVE_INTERVAL_SECONDS) {
			save();
			lastSaveTime = System.currentTimeMillis();
		}
	}

	private String getReport() {
		return 	NumberFormat.format(experiment.getTicksChronometer().getTotalTicks()) + "\t" +
				experiment.getEcosystem().getNumberOfNarjillos() + "\t" +
				experiment.getEcosystem().getNumberOfFoodPellets();
	}

	private void save() {
		isSaving = true;
		System.out.print("> Saving...");
		FilePersistence.save(experiment);
		System.out.println(" Done.");
		isSaving = false;
	}

	private long generateRandomSeed() {
		return Math.abs(new Random().nextInt() % 1_000_000_000);
	}

	public String getStatistics() {
		return "TPS: " + getTicksInLastSecond() + " / Ticks: " + NumberFormat.format(getTotalTicks());
	}

	private int getTicksInLastSecond() {
		return experiment.getTicksChronometer().getTicksInLastSecond();
	}

	private long getTotalTicks() {
		return experiment.getTicksChronometer().getTotalTicks();
	}
}
