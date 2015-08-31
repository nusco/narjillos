package org.nusco.narjillos.experiment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.SimpleExperiment;
import org.nusco.narjillos.core.chemistry.Element;

public class StatsTest {
	
	@Test
	public void extractsDataFromExperiment() {
		Experiment experiment = new SimpleExperiment();
		for (int i = 0; i < 1000; i++)
			experiment.tick();
		ExperimentHistoryEntry stat = new ExperimentHistoryEntry(experiment);
		
		assertEquals(1000, stat.ticks);
		assertEquals(experiment.getTotalRunningTimeInSeconds(), stat.runningTime);
		assertEquals(experiment.getEcosystem().getNumberOfNarjillos(), stat.numberOfNarjillos);
		assertEquals(experiment.getEcosystem().getNumberOfFoodPellets(), stat.numberOfFoodPellets);
		assertEquals(experiment.getGenePool().getCurrentPool().size(), stat.currentPoolSize);
		assertEquals(experiment.getGenePool().getHistoricalPool().size(), stat.historicalPoolSize);
		assertEquals(experiment.getGenePool().getAverageGeneration(), stat.averageGeneration, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.OXYGEN), stat.oxygen, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.HYDROGEN), stat.hydrogen, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.NITROGEN), stat.nitrogen, 0.0);

		int totalCreatures = stat.o2h + stat.o2n + stat.h2o + stat.h2n + stat.n2o + stat.n2h + stat.z2o + stat.z2h + stat.z2n;
		assertEquals(totalCreatures, stat.numberOfNarjillos);
	}

	@Test
	public void convertsToACsvString() {
		ExperimentHistoryEntry stat = new ExperimentHistoryEntry(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);

		String expected = "1, 2, 3, 4, 5, 6, 7.0, 8.0, 9.0, 10.0, 11, 12, 13, 14, 15, 16, 17, 18, 19";
		assertEquals(expected, stat.toString());
	}
}
