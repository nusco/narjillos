package org.nusco.narjillos.ecosystem;

import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.genomics.GenePoolStats;

public class ExperimentStats {

	private final long ticks;
	private final long runningTime;
	private final GenePoolStats genePoolStats;
	private final int numberOfFoodPieces;

	public ExperimentStats(Experiment experiment) {
		this.ticks = experiment.getTicksChronometer().getTotalTicks();
		this.runningTime = experiment.getTotalRunningTimeInSeconds();
		this.genePoolStats = new GenePoolStats(experiment.getGenePool());
		this.numberOfFoodPieces = experiment.getEcosystem().getNumberOfFoodPieces();
	}

	public static String getHeadersString() {
		return alignLeft("tick")
				+ alignLeft("time")
				+ alignLeft("narj")
				+ alignLeft("food")
				+ alignLeft("avg_gen");
	}

	@Override
	public String toString() {
		return alignLeft(NumberFormat.format(ticks))
				+ alignLeft(NumberFormat.format(runningTime))
				+ alignLeft(genePoolStats.getCurrentPoolSize())
				+ alignLeft(numberOfFoodPieces)
				+ alignLeft(NumberFormat.format(genePoolStats.getAverageGeneration()));
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		ExperimentStats other = (ExperimentStats) obj;
		if (!genePoolStats.equals(other.genePoolStats))
			return false;
		if (numberOfFoodPieces != other.numberOfFoodPieces)
			return false;
		if (runningTime != other.runningTime)
			return false;
		if (ticks != other.ticks)
			return false;
		return true;
	}

	private static String alignLeft(Object label) {
		final String padding = "              ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}}
