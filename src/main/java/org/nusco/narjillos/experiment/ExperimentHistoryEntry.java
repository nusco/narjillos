package org.nusco.narjillos.experiment;

import static org.nusco.narjillos.core.chemistry.Element.HYDROGEN;
import static org.nusco.narjillos.core.chemistry.Element.NITROGEN;
import static org.nusco.narjillos.core.chemistry.Element.OXYGEN;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.FoodPellet;

public class ExperimentHistoryEntry {

	public final long ticks;

	public final long runningTime;

	public final long numberOfNarjillos;

	public final long numberOfFoodPellets;

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

	public ExperimentHistoryEntry(long ticks, long runningTime,
		int numberOfNarjillos, int numberOfFoodPellets,
		double oxygen, double hydrogen, double nitrogen,
		int o2h, int o2n, int h2o, int h2n, int n2o, int n2h, int z2o, int z2h, int z2n) {
		this.ticks = ticks;
		this.runningTime = runningTime;
		this.numberOfNarjillos = numberOfNarjillos;
		this.numberOfFoodPellets = numberOfFoodPellets;
		this.oxygen = oxygen;
		this.hydrogen = hydrogen;
		this.nitrogen = nitrogen;
		this.o2h = o2h;
		this.o2n = o2n;
		this.h2o = h2o;
		this.h2n = h2n;
		this.n2o = n2o;
		this.n2h = n2h;
		this.z2o = z2o;
		this.z2h = z2h;
		this.z2n = z2n;
	}

	public ExperimentHistoryEntry(Experiment experiment) {
		this.ticks = experiment.getTicksChronometer().getTotalTicks();
		this.runningTime = experiment.getTotalRunningTimeInSeconds();
		this.numberOfNarjillos = experiment.getEcosystem().getCount(Narjillo.LABEL);
		this.numberOfFoodPellets = experiment.getEcosystem().getCount(FoodPellet.LABEL);
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
		ExperimentHistoryEntry other = (ExperimentHistoryEntry) obj;
		if (numberOfNarjillos != other.numberOfNarjillos || numberOfFoodPellets != other.numberOfFoodPellets)
			return false;
		if (ticks != other.ticks || runningTime != other.runningTime)
			return false;
		if (Double.doubleToLongBits(oxygen) != Double.doubleToLongBits(other.oxygen))
			return false;
		if (Double.doubleToLongBits(hydrogen) != Double.doubleToLongBits(other.hydrogen))
			return false;
		if (Double.doubleToLongBits(nitrogen) != Double.doubleToLongBits(other.nitrogen))
			return false;
		return !(h2n != other.h2n || h2o != other.h2o || n2h != other.n2h || n2o != other.n2o || o2h != other.o2h || o2n != other.o2n
			|| z2h != other.z2h || z2n != other.z2n || z2o != other.z2o);
	}

	@Override
	public String toString() {
		return "" +
			ticks + "," +
			runningTime + "," +
			numberOfNarjillos + "," +
			numberOfFoodPellets + "," +
			oxygen + "," +
			hydrogen + "," +
			nitrogen + "," +
			o2h + "," +
			o2n + "," +
			h2o + "," +
			h2n + "," +
			n2o + "," +
			n2h + "," +
			z2o + "," +
			z2h + "," +
			z2n;
	}

	public static String toCsvHeader() {
		return "Ticks,RunningTime," +
			"NumberOfNarjillos,NumberOfFoodPellets," +
			"Oxygen,Hydrogen,Nitrogen," +
			"O2H,O2N,H2O,H2N,N2O,N2H,Z2O,Z2H,Z2N";
	}

	private Map<String, Integer> getChemicalCycles(Ecosystem ecosystem) {
		Map<String, Integer> result = new LinkedHashMap<>();

		for (String chemicalCycle : Element.CYCLES)
			result.put(chemicalCycle, 0);

		ecosystem.getThings(Narjillo.LABEL).forEach(thing -> {
			Narjillo narjillo = (Narjillo) thing;
			String cycle = "" + narjillo.getBreathedElement() + "2" + narjillo.getByproduct();
			result.put(cycle, result.get(cycle) + 1);
		});

		return result;
	}
}
