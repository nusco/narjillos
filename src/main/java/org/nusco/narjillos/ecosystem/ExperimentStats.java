package org.nusco.narjillos.ecosystem;

import static org.nusco.narjillos.ecosystem.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.ecosystem.chemistry.Element.OXYGEN;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.nusco.narjillos.core.utilities.NumberFormat;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.genomics.GenePoolStats;

public class ExperimentStats {

	private static String[] CHEMICAL_CYCLES = new String[] {
		"O->H",
		"O->N",
		"H->O",
		"H->N",
		"N->O",
		"N->H",
		"Z->O",
		"Z->H",
		"Z->N"
	};
	
	private final long ticks;
	private final long runningTime;
	private final int numberOfFoodPieces;
	private final GenePoolStats genePoolStats;
	private final double oxygen;
	private final double hydrogen;
	private final double nitrogen;
	private final Map<String, Integer> chemicalCycles;

	public ExperimentStats(Experiment experiment) {
		this.ticks = experiment.getTicksChronometer().getTotalTicks();
		this.runningTime = experiment.getTotalRunningTimeInSeconds();
		this.numberOfFoodPieces = experiment.getEcosystem().getNumberOfFoodPieces();
		this.genePoolStats = new GenePoolStats(experiment.getGenePool());
		this.oxygen = experiment.getEcosystem().getAtmosphere().getDensityOf(OXYGEN);
		this.hydrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(HYDROGEN);
		this.nitrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(NITROGEN);
		this.chemicalCycles = calculateChemicalCycles(experiment);
	}

	private Map<String, Integer> calculateChemicalCycles(Experiment experiment) {
		Map<String, Integer> result = new LinkedHashMap<>();
		for (int i = 0; i < CHEMICAL_CYCLES.length; i++)
			result.put(CHEMICAL_CYCLES[i], 0);
		
		Set<Narjillo> narjillos = experiment.getEcosystem().getNarjillos();
		for (Narjillo narjillo : narjillos) {
			String cycles = "" + narjillo.getBreathedElement() + "->" + narjillo.getByproduct();
			result.put(cycles, result.get(cycles) + 1);
		}
		return result;
	}

	public String toCSVLine() {
		StringBuffer result = new StringBuffer();
		result.append("" + ticks + "," + runningTime + "," + numberOfFoodPieces + "," + genePoolStats.toCSVLine() + ","
				+ NumberFormat.format(oxygen) + "," + NumberFormat.format(hydrogen) + "," + NumberFormat.format(nitrogen));
		for (String cycle : chemicalCycles.keySet())
			result.append("," + cycle);
		return result.toString();
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(alignLeft(NumberFormat.format(ticks)) + alignLeft(NumberFormat.format(runningTime))
				+ alignLeft(genePoolStats.getCurrentPoolSize()) + alignLeft(numberOfFoodPieces)
				+ alignLeft(NumberFormat.format(genePoolStats.getAverageGeneration())) + alignLeft(NumberFormat.format(oxygen))
				+ alignLeft(NumberFormat.format(hydrogen)) + alignLeft(NumberFormat.format(nitrogen)));
		for (String cycle : chemicalCycles.keySet())
			result.append(alignLeft(chemicalCycles.get(cycle)));
		return result.toString();
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
		for (String cycle : CHEMICAL_CYCLES)
			if (!chemicalCycles.get(cycle).equals(other.chemicalCycles.get(cycle)))
				return false;
		return true;
	}

	public static String getConsoleHeader() {
		StringBuffer result = new StringBuffer();
		result.append(alignLeft("tick")
				+ alignLeft("time")
				+ alignLeft("narj")
				+ alignLeft("food")
				+ alignLeft("gen")
				+ alignLeft("O")
				+ alignLeft("H")
				+ alignLeft("N"));
		for (int i = 0; i < CHEMICAL_CYCLES.length; i++)
			result.append(alignLeft(CHEMICAL_CYCLES[i]));
		return result.toString();
	}

	public static String getCSVHeader() {
		StringBuffer result = new StringBuffer();
		result.append("ticks,running_time,food," + GenePoolStats.getCSVHeader() + ",O,H,N");
		for (int i = 0; i < CHEMICAL_CYCLES.length; i++)
			result.append("" + CHEMICAL_CYCLES[i]);
		return result.toString();
	}

	public String getChemicalCyclesReport() {
		StringBuffer result = new StringBuffer("Chemical cycles:");
		for (String cycle : chemicalCycles.keySet())
			System.out.println("  " + cycle + "\tspecimen: " + chemicalCycles.get(cycle));
		return result.toString();
	}

	private static String alignLeft(Object label) {
		final String padding = "       ";
		String paddedLabel = padding + label.toString();
		return paddedLabel.substring(paddedLabel.length() - padding.length());
	}
}
