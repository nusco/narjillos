package org.nusco.narjillos.genomics;

/**
 * Gets statistics from a GenePool.
 */
public class GenePoolStats {

	private int currentPoolSize;
	private int historicalPoolSize;

	public GenePoolStats(GenePool genePool) {
		this.currentPoolSize = genePool.getCurrentPool().size();
		this.historicalPoolSize = genePool.getHistoricalPool().size();
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
}
