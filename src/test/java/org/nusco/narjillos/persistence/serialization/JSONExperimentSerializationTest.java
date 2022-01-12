package org.nusco.narjillos.persistence.serialization;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nusco.narjillos.creature.Egg;
import org.nusco.narjillos.creature.Narjillo;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.SimpleExperiment;
import org.nusco.narjillos.experiment.environment.FoodPellet;

public class JSONExperimentSerializationTest {

	@Test
	public void serializesAndDeserializesExperiment() {
		Experiment experiment = new SimpleExperiment();

		for (int i = 0; i < 10; i++) {
			experiment.tick();
			experiment.saveHistoryEntry();
		}

		String json = JSON.toJson(experiment, Experiment.class);
		Experiment deserialized = JSON.fromJson(json, Experiment.class);

		assertThat(deserialized.getId()).isEqualTo(experiment.getId());
		assertThat(deserialized.getTotalRunningTimeInSeconds()).isEqualTo(experiment.getTotalRunningTimeInSeconds());
		assertThat(deserialized.getEcosystem().getCount(Egg.LABEL)).isEqualTo(experiment.getEcosystem().getCount(Egg.LABEL));
		assertThat(deserialized.getEcosystem().getCount(FoodPellet.LABEL)).isEqualTo(experiment.getEcosystem().getCount(FoodPellet.LABEL));
		assertThat(deserialized.getEcosystem().getCount(Narjillo.LABEL)).isEqualTo(experiment.getEcosystem().getCount(Narjillo.LABEL));
		assertThat(deserialized.getTicksChronometer().getTotalTicks()).isEqualTo(10);
	}
}
