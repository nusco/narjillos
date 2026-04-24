package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
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

		assertEquals(experiment.getId(), deserialized.getId());
		assertEquals(experiment.getTotalRunningTimeInSeconds(), deserialized.getTotalRunningTimeInSeconds());
		assertEquals(experiment.getEcosystem().getCount(Egg.LABEL), deserialized.getEcosystem().getCount(Egg.LABEL));
		assertEquals(experiment.getEcosystem().getCount(FoodPellet.LABEL), deserialized.getEcosystem().getCount(FoodPellet.LABEL));
		assertEquals(experiment.getEcosystem().getCount(Narjillo.LABEL), deserialized.getEcosystem().getCount(Narjillo.LABEL));
		assertEquals(10, deserialized.getTicksChronometer().getTotalTicks());
	}
}
