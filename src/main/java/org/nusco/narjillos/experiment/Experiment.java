package org.nusco.narjillos.experiment;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.core.utilities.Chronometer;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.genomics.GenePoolWithHistory;
import org.nusco.narjillos.genomics.SimpleGenePool;

public class Experiment {

	private final String id;
	private final Ecosystem ecosystem;
	private final GenePool genePool;
	private final Chronometer ticksChronometer = new Chronometer();
	private final RanGen ranGen;
	private final boolean trackHistory;
	private final LinkedList<ExperimentStats> stats = new LinkedList<>();

	private long totalRunningTime = 0;
	private transient long lastRegisteredRunningTime;

	public Experiment(long seed, Ecosystem ecosystem, String version, boolean trackHistory, String dna) {
		this(seed, version, ecosystem, trackHistory);
		ecosystem.populate(dna, genePool, ranGen);
	}

	public Experiment(long seed, Ecosystem ecosystem, String version, boolean trackHistory) {
		this(seed, version, ecosystem, trackHistory);
		ecosystem.populate(genePool, ranGen);
	}

	private Experiment(long seed, String version, Ecosystem ecosystem, boolean trackHistory) {
		genePool = initializeGenePool(trackHistory);
		id = "" + seed + "-" + version;
		timeStamp();
		ranGen = new RanGen(seed);
		this.ecosystem = ecosystem;
		this.trackHistory = trackHistory;
	}

	public final void timeStamp() {
		lastRegisteredRunningTime = System.currentTimeMillis();
	}

	public synchronized String getId() {
		return id;
	}

	public void tick() {
		if (ticksChronometer.getTotalTicks() % Configuration.ECOSYSTEM_UPDATE_FOOD_TARGETS_INTERVAL == 0)
			ecosystem.updateTargets();

		ecosystem.tick(genePool, ranGen);
		ticksChronometer.tick();
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public Chronometer getTicksChronometer() {
		return ticksChronometer;
	}

	public long getTotalRunningTimeInSeconds() {
		return totalRunningTime / 1000L;
	}

	public ExperimentStats getStats() {
		return stats.getLast();
	}

	public List<ExperimentStats> getHistory() {
		return stats;
	}

	public String terminate() {
		ecosystem.terminate();

		updateTotalRunningTime();
		String result = toString() + " interrupted at " + getTotalRunningTimeInSeconds() + " seconds, "
			+ getTicksChronometer().getTotalTicks() + " ticks";
		if (!thereAreSurvivors())
			result += " (EXTINCTION)";
		return result;
	}

	public GenePool getGenePool() {
		return genePool;
	}

	public void updateStats() {
		if (!trackHistory)
			stats.clear();
		
		updateTotalRunningTime();
		stats.add(new ExperimentStats(this));
	}

	public boolean thereAreSurvivors() {
		return ecosystem.getNumberOfNarjillos() > 0 || ecosystem.getNumberOfEggs() > 0;
	}

	// for testing
	public void resetTotalRunningTime() {
		totalRunningTime = 0;
	}

	@Override
	public String toString() {
		return "Experiment " + getId();
	}

	private void updateTotalRunningTime() {
		long updateTime = System.currentTimeMillis();
		totalRunningTime = totalRunningTime + (updateTime - lastRegisteredRunningTime);
		lastRegisteredRunningTime = updateTime;
	}

	private GenePool initializeGenePool(boolean trackHistory) {
		if (!trackHistory)
			return new SimpleGenePool();

		return new GenePoolWithHistory();
	}
}
