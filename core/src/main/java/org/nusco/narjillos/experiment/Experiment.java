package org.nusco.narjillos.experiment;

import org.nusco.narjillos.creature.genetics.DNA;
import org.nusco.narjillos.ecosystem.Ecosystem;
import org.nusco.narjillos.shared.physics.Vector;
import org.nusco.narjillos.shared.utilities.Chronometer;
import org.nusco.narjillos.shared.utilities.RanGen;

public strictfp class Experiment {

	private final static int ECOSYSTEM_SIZE = 40_000;
	private static final int INITIAL_NUMBER_OF_FOOD_PIECES = 400;
	private static final int INITIAL_NUMBER_OF_NARJILLOS = 300;

	private final String id;
	private final Ecosystem ecosystem;
	private final Chronometer ticksChronometer = new Chronometer();
	private final RanGen ranGen;
	
	private long totalRunningTime = 0;
	private long lastRegisteredRunningTime;

	public Experiment(String gitCommit, long seed) {
		this(gitCommit, seed, null);
	}

	public Experiment(String gitCommit, long seed, DNA dna) {
		id = gitCommit + "-" + seed;
		if (dna == null)
			System.out.println("Experiment " + id);
		lastRegisteredRunningTime = System.currentTimeMillis();
		ranGen = new RanGen(seed);
		ecosystem = new Ecosystem(ECOSYSTEM_SIZE);
		populate(ecosystem, dna);
	}

	public synchronized String getId() {
		return id;
	}

	private void populate(Ecosystem ecosystem, DNA dna) {
		for (int i = 0; i < INITIAL_NUMBER_OF_FOOD_PIECES; i++)
			ecosystem.spawnFood(randomPosition(ecosystem.getSize()));

		if (dna == null) {
			for (int i = 0; i < INITIAL_NUMBER_OF_NARJILLOS; i++)
				ecosystem.spawnNarjillo(DNA.random(ranGen), randomPosition(ecosystem.getSize()));
		} else {
			for (int i = 0; i < 10; i++)
				ecosystem.spawnNarjillo(dna, randomPosition(ecosystem.getSize()));
		}
	}

	private Vector randomPosition(long size) {
		return Vector.cartesian(ranGen.nextDouble() * size, ranGen.nextDouble() * size);
	}

	public boolean tick() {
		getEcosystem().tick(ranGen);
		ticksChronometer.tick();
		if (ticksChronometer.getTotalTicks() % 1000 == 0)
			executePeriodicOperations();
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
		return getEcosystem().getNumberOfNarjillos() > 0;
	}

	public long getTotalRunningTimeInSeconds() {
		return totalRunningTime / 1000L;
	}

	public void stop() {
		updateTotalRunningTime();
	}

	private void updateTotalRunningTime() {
		long updateTime = System.currentTimeMillis();
		totalRunningTime = totalRunningTime + (updateTime - lastRegisteredRunningTime);
		lastRegisteredRunningTime = updateTime;
	}
}
