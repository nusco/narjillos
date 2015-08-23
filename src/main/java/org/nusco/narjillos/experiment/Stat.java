package org.nusco.narjillos.experiment;

import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class Stat {

	public final long ticks;
	public final long runningTime;
	public final int numberOfNarjillos;
	public final int numberOfFoodPellets;
	public final int currentPoolSize;
	public final int historicalPoolSize;
	public final double averageGeneration;
	public final double oxygen;
	public final double hydrogen;
	public final double nitrogen;
	public final int o2h;
	public final int o2n;
	public final int h2o;
	public final int h2n;
	public final int n2o;
	public final int n2h;
	public final int z2o;
	public final int z2h;
	public final int z2n;

	public Stat(Experiment experiment) {
		this.ticks = experiment.getTicksChronometer().getTotalTicks();
		this.runningTime = experiment.getTotalRunningTimeInSeconds();
		this.numberOfNarjillos = experiment.getEcosystem().getNumberOfNarjillos();
		this.numberOfFoodPellets = experiment.getEcosystem().getNumberOfFoodPellets();
		this.currentPoolSize = experiment.getGenePool().getCurrentPool().size();
		this.historicalPoolSize = experiment.getGenePool().getHistoricalPool().size();
		this.averageGeneration = experiment.getGenePool().getAverageGeneration();
		this.oxygen = experiment.getEcosystem().getAtmosphere().getDensityOf(OXYGEN);
		this.hydrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(HYDROGEN);
		this.nitrogen = experiment.getEcosystem().getAtmosphere().getDensityOf(NITROGEN);

		Map<String, Integer> chemicalCycles = getChemicalCycles(experiment.getEcosystem());
		this.o2h = chemicalCycles.get("O2H");
		this.o2n = chemicalCycles.get("O2N");
		this.h2o = chemicalCycles.get("H2O");
		this.h2n = chemicalCycles.get("H2N");
		this.n2o = chemicalCycles.get("N2O");
		this.n2h = chemicalCycles.get("N2H");
		this.z2o = chemicalCycles.get("Z2O");
		this.z2h = chemicalCycles.get("Z2H");
		this.z2n = chemicalCycles.get("Z2N");
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Stat other = (Stat) obj;
		if (numberOfNarjillos != other.numberOfNarjillos || numberOfFoodPellets != other.numberOfFoodPellets)
			return false;
		if (ticks != other.ticks || runningTime != other.runningTime)
			return false;
		if (Double.doubleToLongBits(averageGeneration) != Double.doubleToLongBits(other.averageGeneration))
			return false;
		if (currentPoolSize != other.currentPoolSize || historicalPoolSize != other.historicalPoolSize)
			return false;
		if (Double.doubleToLongBits(oxygen) != Double.doubleToLongBits(other.oxygen))
			return false;
		if (Double.doubleToLongBits(hydrogen) != Double.doubleToLongBits(other.hydrogen))
			return false;
		if (Double.doubleToLongBits(nitrogen) != Double.doubleToLongBits(other.nitrogen))
			return false;
		if (h2n != other.h2n || h2o != other.h2o || n2h != other.n2h || n2o != other.n2o || o2h != other.o2h || o2n != other.o2n
				|| z2h != other.z2h || z2n != other.z2n || z2o != other.z2o)
			return false;
		return true;
	}

	private Map<String, Integer> getChemicalCycles(Ecosystem ecosystem) {
		Map<String, Integer> result = new LinkedHashMap<>();

		for (String chemicalCycle : Element.CYCLES)
			result.put(chemicalCycle, 0);

		for (Narjillo narjillo : ecosystem.getNarjillos()) {
			String cycle = "" + narjillo.getBreathedElement() + "2" + narjillo.getByproduct();
			result.put(cycle, result.get(cycle) + 1);
		}

		return result;
	}
}
