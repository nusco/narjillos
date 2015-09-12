package org.nusco.narjillos.experiment;

import java.util.LinkedList;
import java.util.List;

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
	public void saveEntry(Experiment experiment) {
	}

	@Override
	public void close() {
	}

	@Override
	public void delete() {
	}
}
