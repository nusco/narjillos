package org.nusco.narjillos.experiment;

import org.nusco.narjillos.core.configuration.Configuration;
import org.nusco.narjillos.core.utilities.Version;
import org.nusco.narjillos.experiment.environment.Ecosystem;
import org.nusco.narjillos.genomics.VolatileDNALog;

public class SimpleExperiment extends Experiment {

	public SimpleExperiment() {
		super(1234, new Ecosystem(Configuration.ECOSYSTEM_BLOCKS_PER_EDGE_IN_APP * 1000L, false), "simple_experiment-" + Version.read());
		setDnaLog(new VolatileDNALog());
		setHistoryLog(new VolatileHistoryLog());
		populate();
	}
}
