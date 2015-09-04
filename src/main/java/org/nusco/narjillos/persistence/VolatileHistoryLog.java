package org.nusco.narjillos.persistence;

import java.util.LinkedList;
import java.util.List;

import org.nusco.narjillos.experiment.Experiment;
import org.nusco.narjillos.experiment.HistoryLog;
import org.nusco.narjillos.experiment.ExperimentHistoryEntry;

public class VolatileHistoryLog implements HistoryLog {

	@Override
	public List<ExperimentHistoryEntry> getEntries() {
		return new LinkedList<>();
	}

	@Override
	public ExperimentHistoryEntry getLatestEntry() {
		return null;
	}

	@Override
	public void saveEntries(Experiment experiment) {
	}

	@Override
	public void close() {
	}

	@Override
	public void clear() {
	}
}
