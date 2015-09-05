package org.nusco.narjillos.experiment;

import java.util.List;

/**
 * A list of stats.
 */
public interface HistoryLog {

	public List<ExperimentHistoryEntry> getEntries();
	public ExperimentHistoryEntry getLatestEntry();
	public void saveEntries(Experiment experiment);
	public void close();
	public void delete();
}