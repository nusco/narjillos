package org.nusco.narjillos.experiment;

import org.nusco.narjillos.core.utilities.Chronometer;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.GenePool;

public class Experiment {

	private final String id;
	private final Ecosystem ecosystem;
	private final Chronometer ticksChronometer = new Chronometer();
	private final NumGen numGen;
	private long totalRunningTime = 0;

	private transient long lastRegisteredRunningTime;
	private transient GenePool genePool;
	private transient HistoryLog historyLog;
	private transient String dna;
	
	public Experiment(long seed, Ecosystem ecosystem, String version, String dna) {
		this(seed, version, ecosystem);
		this.dna = dna;
	}

	public Experiment(long seed, Ecosystem ecosystem, String version) {
		this(seed, version, ecosystem);
		this.dna = null;  // FIXME: ugly. it must go.
	}

	private Experiment(long seed, String version, Ecosystem ecosystem) {
		id = "" + seed + "-" + version;
		timeStamp();
		numGen = new NumGen(seed);
		this.ecosystem = ecosystem;
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

		ecosystem.tick(genePool, numGen);
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

	public String terminate() {
		genePool.terminate();
		historyLog.close();
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

	// TODO: move to the dish?
	public void saveHistoryEntry() {
		updateTotalRunningTime();
		historyLog.saveEntries(this);
	}

	public boolean thereAreSurvivors() {
		return ecosystem.getNumberOfNarjillos() > 0 || ecosystem.getNumberOfEggs() > 0;
	}

	// for testing
	public void resetTotalRunningTime() {
		totalRunningTime = 0;
	}

	public void setGenePool(GenePool genePool) {
		this.genePool = genePool;
		if (dna != null)
			ecosystem.populate(dna, genePool, numGen);
		else
			ecosystem.populate(genePool, numGen);
	}

	public void setHistoryLog(HistoryLog historyLog) {
		this.historyLog = historyLog;
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
}
