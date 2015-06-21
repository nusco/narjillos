package org.nusco.narjillos.genomics;

import java.util.List;

import org.nusco.narjillos.core.utilities.NumberFormat;

/**
 * Gets statistics from a GenePool.
 */
public class GenePoolStats {

	private final int currentPoolSize;
	private final int historicalPoolSize;
	private final double averageGeneration;

	public GenePoolStats(GenePool genePool) {
		this.currentPoolSize = genePool.getCurrentPool().size();
		this.historicalPoolSize = genePool.getHistoricalPool().size();
		this.averageGeneration = calculateAverageGeneration(genePool);
	}

	public int getCurrentPoolSize() {
		return currentPoolSize;
	}

	public int getHistoricalPoolSize() {
		return historicalPoolSize;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Current gene pool size     => " + getCurrentPoolSize() + "\n");
		result.append("Historical gene pool size  => " + getHistoricalPoolSize() + "\n");
		return result.toString();
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		GenePoolStats other = (GenePoolStats) obj;
		if (currentPoolSize != other.currentPoolSize)
			return false;
		if (historicalPoolSize != other.historicalPoolSize)
			return false;
		return true;
	}

	public double getAverageGeneration() {
		return averageGeneration;
	}

	private double calculateAverageGeneration(GenePool genePool) {
		if (currentPoolSize == 0)
			return 0;
		
		List<Long> currentPool = genePool.getCurrentPool();
		double generationsSum = 0;
		for (Long id : currentPool) {
			DNA dna = genePool.getDna(id);
			generationsSum += genePool.getGenerationOf(dna);
		}
		return generationsSum / currentPool.size();
	}

	public String toCSVLine() {
		return "" + getCurrentPoolSize() + ","
				+ getHistoricalPoolSize() + ","
				+ NumberFormat.format(getAverageGeneration());
	}

	public static String getCSVHeader() {
		return "population,historical_pool_size,average_generation";
	}
}
