package org.nusco.narjillos.experiment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.experiment.environment.Ecosystem;

public class StatsTest {
	
	Experiment experiment;
	
	@Before
	public void prepareExperiment() {
		experiment = new Experiment(123, new Ecosystem(1000, false), "1.1.1", false);
		for (int i = 0; i < 1000; i++)
			experiment.tick();
	}
	
	@Test
	public void extractsDataFromExperiment() {
		Stat stats = new Stat(experiment);
		
		assertEquals(1000, stats.ticks);
		assertEquals(experiment.getTotalRunningTimeInSeconds(), stats.runningTime);
		assertEquals(experiment.getEcosystem().getNumberOfNarjillos(), stats.numberOfNarjillos);
		assertEquals(experiment.getEcosystem().getNumberOfFoodPellets(), stats.numberOfFoodPellets);
		assertEquals(experiment.getGenePool().getCurrentPool().size(), stats.currentPoolSize);
		assertEquals(experiment.getGenePool().getHistoricalPool().size(), stats.historicalPoolSize);
		assertEquals(experiment.getGenePool().getAverageGeneration(), stats.averageGeneration, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.OXYGEN), stats.oxygen, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.HYDROGEN), stats.hydrogen, 0.0);
		assertEquals(experiment.getEcosystem().getAtmosphere().getDensityOf(Element.NITROGEN), stats.nitrogen, 0.0);

		int totalCreatures = stats.o2h + stats.o2n + stats.h2o + stats.h2n + stats.n2o + stats.n2h + stats.z2o + stats.z2h + stats.z2n;
		assertEquals(totalCreatures, stats.numberOfNarjillos);
	}
}
