package org.nusco.narjillos.experiment;

import org.nusco.narjillos.core.utilities.Chronometer;
import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.DNALog;

public class Experiment {

	private final String id;

	private final Ecosystem ecosystem;

	private final Chronometer ticksChronometer = new Chronometer();

	private final NumGen numGen;

	private long totalRunningTime = 0;

	private transient DNALog dnaLog;

	private transient HistoryLog historyLog;

	private transient long lastRegisteredRunningTime;

	public Experiment(long seed, Ecosystem ecosystem, String version) {
		this(seed, version, ecosystem);
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

		ecosystem.tick(dnaLog, numGen);
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
		ecosystem.terminate();

		updateTotalRunningTime();
		String result = toString() + " interrupted at " + getTotalRunningTimeInSeconds() + " seconds, "
			+ getTicksChronometer().getTotalTicks() + " ticks";
		if (lifeIsExtinct())
			result += " (EXTINCTION)";
		return result;
	}

	// TODO: move to the dish?
	public void saveHistoryEntry() {
		updateTotalRunningTime();
		historyLog.saveEntry(this);
	}

	public boolean lifeIsExtinct() {
		return ecosystem.getNumberOfNarjillos() == 0 && ecosystem.getNumberOfEggs() == 0;
	}

	// for testing
	public void resetTotalRunningTime() {
		totalRunningTime = 0;
	}

	public final void setDnaLog(DNALog dnaLog) {
		this.dnaLog = dnaLog;
	}

	public final void populate(String dna) {
		ecosystem.populate(dna, dnaLog, numGen);
	}

	public final void populate() {
		ecosystem.populate(dnaLog, numGen);
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
