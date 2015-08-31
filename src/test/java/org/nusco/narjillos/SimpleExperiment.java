package org.nusco.narjillos;

import org.nusco.narjillos.core.utilities.Configuration;
import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.SimpleGenePool;
import org.nusco.narjillos.persistence.VolatileHistoryLog;

public class SimpleExperiment extends Experiment {

	public SimpleExperiment() {
		super(1234, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000, false), "deterministic_experiment_test");
		setGenePool(new SimpleGenePool());
		setHistoryLog(new VolatileHistoryLog());
	}
}
