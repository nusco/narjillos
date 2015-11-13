package org.nusco.narjillos.persistence.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.SimpleExperiment;

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
		assertEquals(experiment.getEcosystem().getNumberOfEggs(), deserialized.getEcosystem().getNumberOfEggs());
		assertEquals(experiment.getEcosystem().getNumberOfFoodPellets(), deserialized.getEcosystem().getNumberOfFoodPellets());
		assertEquals(experiment.getEcosystem().getNumberOfNarjillos(), deserialized.getEcosystem().getNumberOfNarjillos());
		assertEquals(10, deserialized.getTicksChronometer().getTotalTicks());
	}
}
