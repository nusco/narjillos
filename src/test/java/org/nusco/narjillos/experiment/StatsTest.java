package org.nusco.narjillos.experiment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.chemistry.Atmosphere;
import org.nusco.narjillos.core.chemistry.Element;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.experiment.environment.FoodPellet;

public class StatsTest {

	@Test
	public void extractsDataFromExperiment() {
		Experiment experiment = new SimpleExperiment();
		for (int i = 0; i < 1000; i++)
			experiment.tick();

		var stat = new ExperimentHistoryEntry(experiment);

		assertThat(stat.ticks).isEqualTo(1000);
		assertThat(stat.runningTime).isEqualTo(experiment.getTotalRunningTimeInSeconds());

		Ecosystem ecosystem = experiment.getEcosystem();
		assertThat(stat.numberOfNarjillos).isEqualTo(ecosystem.getCount(Narjillo.LABEL));
		assertThat(stat.numberOfFoodPellets).isEqualTo(ecosystem.getCount(FoodPellet.LABEL));

		Atmosphere atmosphere = ecosystem.getAtmosphere();
		assertThat(stat.oxygen).isEqualTo(atmosphere.getDensityOf(Element.OXYGEN));
		assertThat(stat.hydrogen).isEqualTo(atmosphere.getDensityOf(Element.HYDROGEN));
		assertThat(stat.nitrogen).isEqualTo(atmosphere.getDensityOf(Element.NITROGEN));

		int totalCreatures = stat.o2h + stat.o2n + stat.h2o + stat.h2n + stat.n2o + stat.n2h + stat.z2o + stat.z2h + stat.z2n;

		assertThat(stat.numberOfNarjillos).isEqualTo(totalCreatures);
	}

	@Test
	public void convertsToACsvString() {
		var stat = new ExperimentHistoryEntry(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

		var expected = "1,2,3,4,5.0,6.0,7.0,8,9,10,11,12,13,14,15,16";

		assertThat(stat.toString()).isEqualTo(expected);
	}
}
