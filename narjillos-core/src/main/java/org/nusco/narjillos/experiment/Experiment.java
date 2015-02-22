package org.nusco.narjillos.experiment;

import org.nusco.narjillos.creature.body.physics.Viscosity;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.genomics.GenePool;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.RanGen;

public class Experiment {

	private final static int ECOSYSTEM_SIZE = 40_000;
	private static final int INITIAL_NUMBER_OF_FOOD_PIECES = 400;
	private static final int INITIAL_NUMBER_OF_NARJILLOS = 300;

	private final String id;
	private final Ecosystem ecosystem;
	private final Chronometer ticksChronometer = new Chronometer();
	private final RanGen ranGen;

	private long totalRunningTime = 0;
	private GenePool genePool = new GenePool();
	private transient long lastRegisteredRunningTime;

	public Experiment(long seed, String version) {
		this(seed, version, null, true);
	}

	public Experiment(long seed, String version, String dna, boolean trackGenePool) {
		initializeGenePool(trackGenePool);
		
		id = "" + seed + "-" + version;
		timeStamp();
		ranGen = new RanGen(seed);
		ecosystem = new Ecosystem(ECOSYSTEM_SIZE);
		populate(ecosystem, dna);
		
		// check that things cannot move faster than a space area in a single
		// tick (which would make collision detection unreliable)
		if (ecosystem.getSpaceAreaSize() < Viscosity.getMaxVelocity())
			throw new RuntimeException("Bug: Area size smaller than max velocity");
	}

	private void initializeGenePool(boolean trackGenePool) {
		if (!trackGenePool)
			return;

		genePool.enableTracking();
		DNA.setObserver(genePool);
	}

	public final void timeStamp() {
		lastRegisteredRunningTime = System.currentTimeMillis();
	}

	public synchronized String getId() {
		return id;
	}

	private void populate(Ecosystem ecosystem, String dna) {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_PIECES; i++)
			ecosystem.spawnFood(randomPosition(ecosystem.getSize()));

		if (dna == null) {
			for (int i = 0; i < INITIAL_NUMBER_OF_NARJILLOS; i++)
				ecosystem.spawnEgg(DNA.random(ranGen), randomPosition(ecosystem.getSize()), ranGen);
		} else {
			for (int i = 0; i < INITIAL_NUMBER_OF_NARJILLOS; i++)
				ecosystem.spawnEgg(new DNA(dna), randomPosition(ecosystem.getSize()), ranGen);
		}
	}

	private Vector randomPosition(long size) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	public boolean tick() {
		if (ticksChronometer.getTotalTicks() % 1000 == 0)
			executePeriodicOperations();

		getEcosystem().tick(ranGen);
		ticksChronometer.tick();
		return areThereSurvivors();
	}

	private void executePeriodicOperations() {
		getEcosystem().updateAllTargets();
		updateTotalRunningTime();
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public Chronometer getTicksChronometer() {
		return ticksChronometer;
	}

	private boolean areThereSurvivors() {
		return getEcosystem().getNumberOfNarjillos() > 0 || getEcosystem().getNumberOfEggs() > 0;
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
		return toString() + " ending at " + getTotalRunningTimeInSeconds() + " seconds, "
				+ getTicksChronometer().getTotalTicks() + " ticks";
	}

	public GenePool getGenePool() {
		return genePool;
	}

	// For serialization only. Don't call this in other situations, if you don't
	// want to make the experiment non-deterministic.
	public void setGenePool(GenePool genePool) {
		this.genePool = genePool;
		DNA.setObserver(genePool);
	}

	@Override
	public String toString() {
		return "Experiment " + getId();
	}
}
