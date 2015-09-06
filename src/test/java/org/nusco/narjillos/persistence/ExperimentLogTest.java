package org.nusco.narjillos.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nusco.narjillos.SimpleExperiment;
import org.nusco.narjillos.application.Version;
import org.nusco.narjillos.experiment.Experiment;

public class ExperimentLogTest {

	private ExperimentLog experimentLog;

	@Before
	public void createDababase() {
		experimentLog = new ExperimentLog("test-" + Version.read());
	}

	@After
	public void deleteDatabase() {
		experimentLog.delete();
	}
	
	@Test
	public void testSavesAndLoadsExperiments() {
		Experiment experiment = new SimpleExperiment();
		experimentLog.save(experiment);

		assertEquals(experiment.getId(), experimentLog.load().getId());
	}
}