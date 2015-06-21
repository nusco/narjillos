package org.nusco.narjillos.ecosystem;

import static org.nusco.narjillos.ecosystem.chemistry.Element.*;

import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.genomics.GenePoolStats;

public class ExperimentStats {

	private final long ticks;
	private final long runningTime;
	private final int numberOfFoodPieces;
	private final GenePoolStats genePoolStats;
	private final double oxygen;
	private final double hydrogen;
	private final double nitrogen;

	public ExperimentStats(Experiment experiment) {
		this.ticks = experiment.getTicksChronometer().getTotalTicks();
		this.runningTime = experiment.getTotalRunningTimeInSeconds();
		this.numberOfFoodPieces = experiment.getEcosystem().getNumberOfFoodPieces();
		this.genePoolStats = new GenePoolStats(experiment.getGenePool());
		this.oxygen = experiment.getEcosystem().getAtmosphere().getDensityOf(OXYGEN);
		this.hydrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(HYDROGEN);
		this.nitrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(NITROGEN);
	}

	public String toCSVLine() {
		return "" + ticks + ","
				+ runningTime + ","
				+ numberOfFoodPieces + ","
				+ genePoolStats.toCSVLine() + ","
				+ NumberFormat.format(oxygen) + ","
				+ NumberFormat.format(hydrogen) + ","
				+ NumberFormat.format(nitrogen);
	}

	@Override
	public String toString() {
		return alignLeft(NumberFormat.format(ticks))
				+ alignLeft(NumberFormat.format(runningTime))
				+ alignLeft(genePoolStats.getCurrentPoolSize())
				+ alignLeft(numberOfFoodPieces)
				+ alignLeft(NumberFormat.format(genePoolStats.getAverageGeneration()))
				+ alignLeft(NumberFormat.format(oxygen))
				+ alignLeft(NumberFormat.format(hydrogen))
				+ alignLeft(NumberFormat.format(nitrogen));
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		ExperimentStats other = (ExperimentStats) obj;
		if (ticks != other.ticks)
			return false;
		if (runningTime != other.runningTime)
			return false;
		if (numberOfFoodPieces != other.numberOfFoodPieces)
			return false;
		if (!genePoolStats.equals(other.genePoolStats))
			return false;
		if (oxygen != other.oxygen)
			return false;
		if (hydrogen != other.hydrogen)
			return false;
		if (nitrogen != other.nitrogen)
			return false;
		return true;
	}

	public static String getConsoleHeader() {
		return alignLeft("tick")
				+ alignLeft("time")
				+ alignLeft("narj")
				+ alignLeft("food")
				+ alignLeft("avg_gen")
				+ alignLeft("O")
				+ alignLeft("H")
				+ alignLeft("N");
	}

	public static String getCSVHeader() {
		return "ticks,running_time,food," + GenePoolStats.getCSVHeader() + ",O,H,N";
	}
	
	private static String alignLeft(Object label) {
		final String padding = "              ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}
}
