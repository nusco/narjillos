package org.nusco.narjillos.experiment.environment;

import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.NumGen;

/**
 * Decides when to spawn food based on the amount of current food and the number of blocks in the environment.
 */
class FoodClock {

	private final double numberOf1000PointBlocks;

	public FoodClock(double numberOf1000PointBlocks) {
		this.numberOf1000PointBlocks = numberOf1000PointBlocks;
	}

	public boolean shouldSpawnFood(long numberOfFoodPellets, NumGen numGen) {
		double maxFoodPellets = numberOf1000PointBlocks * Configuration.ECOSYSTEM_MAX_FOOD_DENSITY_PER_1000_BLOCK;
		if (numberOfFoodPellets >= maxFoodPellets)
			return false;

		double foodRespawnAverageInterval = Configuration.ECOSYSTEM_FOOD_RESPAWN_AVERAGE_INTERVAL_PER_BLOCK / numberOf1000PointBlocks;
		return numGen.nextDouble() < 1.0 / foodRespawnAverageInterval;
	}
}
