package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.core.utilities.Chronometer;
import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.core.utilities.RanGen;
import org.nusco.narjillos.genomics.GenePool;

public class Experiment {

	private final String id;
	private final Ecosystem ecosystem;
	private final Chronometer ticksChronometer = new Chronometer();
	private final RanGen ranGen;

	private long totalRunningTime = 0;
	private GenePool genePool = new GenePool();
	private transient long lastRegisteredRunningTime;

	public Experiment(long seed, Ecosystem ecosystem, String version, boolean trackGenePool, String dna) {
		this(seed, version, ecosystem, trackGenePool);
		ecosystem.populate(dna, genePool, ranGen);
	}

	public Experiment(long seed, Ecosystem ecosystem, String version, boolean trackGenePool) {
		this(seed, version, ecosystem, trackGenePool);
		ecosystem.populate(genePool, ranGen);
	}

	private Experiment(long seed, String version, Ecosystem ecosystem, boolean trackGenePool) {
		initializeGenePool(trackGenePool);
		id = "" + seed + "-" + version;
		timeStamp();
		ranGen = new RanGen(seed);
		this.ecosystem = ecosystem;
	}

	private void initializeGenePool(boolean trackGenePool) {
		if (!trackGenePool)
			return;

		genePool.enableAncestralMemory();
	}

	public final void timeStamp() {
		lastRegisteredRunningTime = System.currentTimeMillis();
	}

	public synchronized String getId() {
		return id;
	}

	public boolean tick() {
		if (ticksChronometer.getTotalTicks() % Configuration.ECOSYSTEM_UPDATE_FOOD_TARGETS_INTERVAL == 0)
			executePeriodicOperations();

		ecosystem.tick(genePool, ranGen);
		ticksChronometer.tick();
		return areThereSurvivors();
	}

	private void executePeriodicOperations() {
		ecosystem.periodicUpdate();
		updateTotalRunningTime();
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public Chronometer getTicksChronometer() {
		return ticksChronometer;
	}

	private boolean areThereSurvivors() {
		return ecosystem.getNumberOfNarjillos() > 0 || ecosystem.getNumberOfEggs() > 0;
	}

	public long getTotalRunningTimeInSeconds() {
		return totalRunningTime / 1000L;
	}

	// for testing
	public void resetTotalRunningTime() {
		totalRunningTime = 0;
	}

	private void updateTotalRunningTime() {
		long updateTime = System.currentTimeMillis();
		totalRunningTime = totalRunningTime + (updateTime - lastRegisteredRunningTime);
		lastRegisteredRunningTime = updateTime;
	}

	public String terminate() {
		updateTotalRunningTime();
		ecosystem.terminate();
		return toString() + " interrupted at " + getTotalRunningTimeInSeconds() + " seconds, "
				+ getTicksChronometer().getTotalTicks() + " ticks";
	}

	public GenePool getGenePool() {
		return genePool;
	}

	@Override
	public String toString() {
		return "Experiment " + getId();
	}
}
