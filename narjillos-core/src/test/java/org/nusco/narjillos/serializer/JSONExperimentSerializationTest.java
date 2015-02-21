package org.nusco.narjillos.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.genomics.DNA;
import org.nusco.narjillos.shared.utilities.RanGen;

public class JSONExperimentSerializationTest {

	@Test
	public void serializesAndDeserializesExperiment() {
		Experiment experiment = new Experiment(1234, "experiment_serialization_test", DNA.random(new RanGen(1)), true);
		for (int i = 0; i < 10; i++)
			experiment.tick();
		
		String json = JSON.toJson(experiment, Experiment.class);
		Experiment deserialized = JSON.fromJson(json, Experiment.class);

		assertEquals(experiment.getId(), deserialized.getId());
		assertEquals(experiment.getTotalRunningTimeInSeconds(), deserialized.getTotalRunningTimeInSeconds());
		assertEquals(experiment.getEcosystem().getNumberOfEggs(), deserialized.getEcosystem().getNumberOfEggs());
		assertEquals(experiment.getEcosystem().getNumberOfFoodPieces(), deserialized.getEcosystem().getNumberOfFoodPieces());
		assertEquals(experiment.getEcosystem().getNumberOfNarjillos(), deserialized.getEcosystem().getNumberOfNarjillos());
		assertEquals(10, deserialized.getTicksChronometer().getTotalTicks());
		assertNotNull(deserialized.getGenePool().getMostSuccessfulDNA());
		assertEquals(experiment.getGenePool().getMostSuccessfulDNA(), deserialized.getGenePool().getMostSuccessfulDNA());
	}
}
