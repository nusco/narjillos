package org.nusco.narjillos.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nusco.narjillos.core.utilities.Version;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.SimpleExperiment;

public class ExperimentLogTest {

	private ExperimentLog experimentLog;

	@BeforeEach
	public void createDababase() {
		experimentLog = new ExperimentLog("test-" + Version.read());
	}

	@AfterEach
	public void deleteDatabase() {
		experimentLog.delete();
	}

	@Test
	public void testSavesAndLoadsExperiments() {
		Experiment experiment = new SimpleExperiment();
		experimentLog.save(experiment);

		assertThat(experimentLog.load().getId()).isEqualTo(experiment.getId());
	}
}
